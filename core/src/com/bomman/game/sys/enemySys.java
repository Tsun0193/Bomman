package com.bomman.game.sys;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.bomman.game.builders.actorBuilder;
import com.bomman.game.components.Enemy;
import com.bomman.game.components.rigidBody;
import com.bomman.game.components.state;
import com.bomman.game.components.transform;
import com.bomman.game.game.gameManager;

public class enemySys extends IteratingSystem {
    protected ComponentMapper<Enemy> mEnemy;
    protected ComponentMapper<rigidBody> mRigidBody;
    protected ComponentMapper<state> mState;
    protected ComponentMapper<transform> mTransform;

    private boolean hit;
    private final Vector2 fromVector;
    private final Vector2 toVector;

    private Enemy enemy;
    private rigidBody rBody;
    private state State;

    private final Vector2[] boss1TargetCorners = {
            new Vector2(3.0f, 7.5f),
            new Vector2(12.0f, 7.5f),
            new Vector2(7.5f, 12.0f),
            new Vector2(7.5f, 5.5f)
    };

    private int boss1CurrentTarget = MathUtils.random(0, 3);

    public enemySys() {
        super(Aspect.all(Enemy.class, transform.class, rigidBody.class, state.class));
        fromVector = new Vector2();
        toVector = new Vector2();
    }

    @Override
    protected void process(int entityId) {
        enemy = mEnemy.get(entityId);
        rBody = mRigidBody.get(entityId);
        State = mState.get(entityId);

        //            case "boss1":
        //                handleBoss1(entityId);
        //                break;
        if ("boss1".equals(enemy.type)) {
            handleBoss1(entityId);
        } else {
            handleBasics(entityId);
        }
    }

    private void handleBasics(int entityId) {
        Body body = rBody.body;

        if (enemy.receivedDmg > 0) {
            enemy.damage(enemy.receivedDmg);
            enemy.receivedDmg = 0;
        }

        if (enemy.hp <= 0) {
            enemy.state = Enemy.State.dead;
            enemy.lifespan = 0;
        } else {
            enemy.lifespan += world.getDelta();
        }

        switch (enemy.state) {
            case attackLeft:
                State.setCurrentState("attackLeft");
                break;
            case attackRight:
                State.setCurrentState("attackRight");
                break;
            case attackUp:
                State.setCurrentState("attackUp");
                break;
            case attackDown:
                State.setCurrentState("attackDown");
                break;
            case dead:
                State.setCurrentState("dead");
                Filter filter = body.getFixtureList().get(0).getFilterData();
                filter.maskBits = gameManager.NOTHING_BIT;
                body.getFixtureList().get(0).setFilterData(filter);

                if (State.getStateTime() <= 0) {
                    gameManager.getInstance().playSound(enemy.getDieSound(), 1.0f, MathUtils.random(0.8f, 1.2f), 0);
                }

                if (State.getStateTime() > 0.6f) {
                    gameManager.enemiesLeft--;

                    if (gameManager.enemiesLeft <= 0) {
                        actorBuilder builder = actorBuilder.init(body.getWorld(), world);
                        builder.createPortal();
                        gameManager.getInstance().playSound("PortalAppears.ogg");
                    }

                    if (Math.random() < 0.2) {
                        actorBuilder builder = actorBuilder.init(body.getWorld(), world);
                        builder.createBuff(body.getPosition().x, body.getPosition().y);
                    }

                    body.getWorld().destroyBody(body);
                    mRigidBody.set(entityId, false);
                    mEnemy.set(entityId, false);
                    mState.set(entityId, false);
                    transform Transform = mTransform.get(entityId);
                    Transform.temp = 999;
                }
                break;
            case moveLeft:
                State.setCurrentState("moveLeft");
                if (body.getLinearVelocity().x > -enemy.getSpeed()) {
                    body.applyLinearImpulse(new Vector2(-enemy.getSpeed() * body.getMass(), 0), body.getWorldCenter(), true);
                }
                if (hitHorizontal(body, fromVector.set(body.getPosition()), toVector.set(body.getPosition().x - 0.5f, body.getPosition().y))) {
                    changeWalkingState(enemy);
                }
                break;
            case moveRight:
                State.setCurrentState("moveRight");
                if (body.getLinearVelocity().x < enemy.getSpeed()) {
                    body.applyLinearImpulse(new Vector2(enemy.getSpeed() * body.getMass(), 0), body.getWorldCenter(), true);
                }
                if (hitHorizontal(body, fromVector.set(body.getPosition()), toVector.set(body.getPosition().x + 0.5f, body.getPosition().y))) {
                    changeWalkingState(enemy);
                }
                break;
            case moveUp:
                State.setCurrentState("moveUp");
                if (body.getLinearVelocity().y < enemy.getSpeed()) {
                    body.applyLinearImpulse(new Vector2(0, enemy.getSpeed() * body.getMass()), body.getWorldCenter(), true);
                }
                if (hitVertical(body, fromVector.set(body.getPosition()), toVector.set(body.getPosition().x, body.getPosition().y + 0.5f))) {
                    changeWalkingState(enemy);
                }
                break;
            case moveDown:
            default:
                State.setCurrentState("moveDown");
                if (body.getLinearVelocity().y > -enemy.getSpeed()) {
                    body.applyLinearImpulse(new Vector2(0, -enemy.getSpeed() * body.getMass()), body.getWorldCenter(), true);
                }
                if (hitVertical(body, fromVector.set(body.getPosition()), toVector.set(body.getPosition().x, body.getPosition().y - 0.5f))) {
                    changeWalkingState(enemy);
                }
                break;
        }
    }

    private void handleBombEnemy(int entityId) {
        Body body = rBody.body;
        actorBuilder actorBuilder = com.bomman.game.builders.actorBuilder.init(body.getWorld(), world);

        if (enemy.receivedDmg > 0) {
            enemy.damage(enemy.receivedDmg);
            enemy.receivedDmg = 0;
        }

        if (enemy.hp <= 0) {
            enemy.state = Enemy.State.dead;
            enemy.lifespan = 0;
        } else {
            enemy.lifespan += world.getDelta();
        }

        if (enemy.lifespan > 12f && gameManager.enemiesLeft <= 16) {
            if (((int) enemy.lifespan) % 12 == 11 && ((int) (enemy.lifespan * 10)) % 10 == 2) {
                if (MathUtils.random() < 0.05f) {
                    enemy.state = Enemy.State.attackUp;
                }
            }
        }

        switch (enemy.state) {
            case attackLeft:
                State.setCurrentState("attackLeft");
                break;
            case attackRight:
                State.setCurrentState("attackRight");
                break;
            case attackUp:
                State.setCurrentState("attackUp");
                if (State.getStateTime() > 3f) {
                    // spawn a bomb-enemy
                    enemy.state = Enemy.State.getRandWalkingState();
                    actorBuilder.createBombEnemy(body.getPosition().x, body.getPosition().y);
                }
                break;
            case attackDown:
                State.setCurrentState("attackDown");
                break;
            case dead:
                State.setCurrentState("dead");
                Filter filter = body.getFixtureList().get(0).getFilterData();
                filter.maskBits = gameManager.NOTHING_BIT;
                body.getFixtureList().get(0).setFilterData(filter);

                if (State.getStateTime() <= 0) {
                    gameManager.getInstance().playSound("Explosion.ogg", 1.0f, MathUtils.random(0.6f, 0.8f), 0);
                    actorBuilder.createExplosion(body.getPosition().x, body.getPosition().y, 1);
                }

                if (State.getStateTime() > 0.6f) {
                    // decrease enemy count
                    gameManager.enemiesLeft--;

                    // if no enemy left, create the portal
                    if (gameManager.enemiesLeft <= 0) {
                        actorBuilder.createPortal();
                        gameManager.getInstance().playSound("PortalAppears.ogg");
                    }

                    // chance to create PowerUp item
                    if (Math.random() < 0.2) {
                        actorBuilder.createBuff(body.getPosition().x, body.getPosition().y);
                    }

                    body.getWorld().destroyBody(body);
                    mRigidBody.set(entityId, false);
                    mEnemy.set(entityId, false);
                    mState.set(entityId, false);
                    transform Transform = mTransform.get(entityId);
                    Transform.temp = 999;

                    world.delete(entityId);
                }
                break;
            case moveLeft:
                State.setCurrentState("moveLeft");
                if (body.getLinearVelocity().x > -enemy.getSpeed()) {
                    body.applyLinearImpulse(new Vector2(-enemy.getSpeed() * body.getMass(), 0), body.getWorldCenter(), true);
                }
                if (hitHorizontal(body, fromVector.set(body.getPosition()), toVector.set(body.getPosition().x - 0.5f, body.getPosition().y))) {
                    changeWalkingState(enemy);
                }
                break;
            case moveRight:
                State.setCurrentState("moveRight");
                if (body.getLinearVelocity().x < enemy.getSpeed()) {
                    body.applyLinearImpulse(new Vector2(enemy.getSpeed() * body.getMass(), 0), body.getWorldCenter(), true);
                }
                if (hitHorizontal(body, fromVector.set(body.getPosition()), toVector.set(body.getPosition().x + 0.5f, body.getPosition().y))) {
                    changeWalkingState(enemy);
                }
                break;
            case moveUp:
                State.setCurrentState("moveUp");
                if (body.getLinearVelocity().y < enemy.getSpeed()) {
                    body.applyLinearImpulse(new Vector2(0, enemy.getSpeed() * body.getMass()), body.getWorldCenter(), true);
                }
                if (hitVertical(body, fromVector.set(body.getPosition()), toVector.set(body.getPosition().x, body.getPosition().y + 0.5f))) {
                    changeWalkingState(enemy);
                }
                break;
            case moveDown:
            default:
                State.setCurrentState("moveDown");
                if (body.getLinearVelocity().y > -enemy.getSpeed()) {
                    body.applyLinearImpulse(new Vector2(0, -enemy.getSpeed() * body.getMass()), body.getWorldCenter(), true);
                }
                if (hitVertical(body, fromVector.set(body.getPosition()), toVector.set(body.getPosition().x, body.getPosition().y - 0.5f))) {
                    changeWalkingState(enemy);
                }
                break;
        }
    }
    private void handleBoss1(int entityId) {
        Body body = rBody.body;
        actorBuilder actorBuilder = com.bomman.game.builders.actorBuilder.init(body.getWorld(), world);

        if (boss1TargetCorners[boss1CurrentTarget].dst2(body.getPosition()) < 0.1f) {
            boss1CurrentTarget = MathUtils.random(0, 3);
        }

        if (enemy.receivedDmg > 0) {
            if (enemy.state != Enemy.State.damaged) {
                enemy.damage(1);    // boss only take 1 damage per time

                // chance to create PowerUp item
                if (Math.random() < 0.2) {
                    actorBuilder.createBuff(body.getPosition().x, body.getPosition().y - 2f);
                }
            }
            enemy.receivedDmg = 0;
            enemy.state = Enemy.State.damaged;
        }

        if (enemy.hp <= 0) {
            enemy.state = Enemy.State.dead;
        }

        enemy.lifespan += world.getDelta();

        // Boss1 attack
        if (enemy.hp > 0 && MathUtils.random() < 0.005) {
            enemy.state = Enemy.State.attackDown;
        }

        switch (enemy.state) {
            case attackLeft:
            case attackRight:
            case attackUp:
            case attackDown:
                State.setCurrentState("attackDown");
                if (State.getStateTime() > 0.6f) {
                    actorBuilder.createExplosion(body.getPosition().x, body.getPosition().y - 4f, 1);
                    gameManager.getInstance().playSound("Boss1Hammer.ogg");
                    changeWalkingState(enemy);
                }
                break;
            case damaged:
                State.setCurrentState("damaged");
                if (State.getStateTime() > 0.2f) {
                    changeWalkingState(enemy);
                }
                break;
            case dead:
                State.setCurrentState("dead");
                Filter filter = body.getFixtureList().get(0).getFilterData();
                filter.maskBits = gameManager.NOTHING_BIT;
                body.getFixtureList().get(0).setFilterData(filter);

                if (State.getStateTime() <= 0) {
                    actorBuilder.createBoss1Explosion(body.getPosition().x, body.getPosition().y);
                    enemy.lifespan = 0;
                }

                if (enemy.lifespan > 0.2f) {
                    gameManager.getInstance().playSound("Explosion.ogg", 1.0f, MathUtils.random(0.9f, 1.1f), 0);
                    enemy.lifespan -= 0.4f;
                }

                if (State.getStateTime() > 2.2f) {
                    // decrease enemy count
                    gameManager.enemiesLeft--;

                    // if no enemy left, create the portal
                    if (gameManager.enemiesLeft <= 0) {
                        actorBuilder.createPortal();
                        gameManager.getInstance().playSound("PortalAppears.ogg");
                        gameManager.getInstance().playMusic("Victory.ogg", false);
                    }

                    body.getWorld().destroyBody(body);
                    world.delete(entityId);
                }
                break;
            case moveLeft:
            case moveRight:
            case moveUp:
            case moveDown:
            default:
                State.setCurrentState("moveDown");
                toVector.set(boss1TargetCorners[boss1CurrentTarget]);
                toVector.sub(body.getPosition());
                toVector.nor();

                if (body.getLinearVelocity().len2() < enemy.getSpeed() * enemy.getSpeed()) {
                    body.applyLinearImpulse(toVector.scl(enemy.getSpeed()), body.getWorldCenter(), true);
                }
                break;
        }
    }
    private boolean hitVertical(Body body, Vector2 set, Vector2 set1) {
        World b2dWorld = body.getWorld();
        hit = false;

        RayCastCallback rayCastCallback = new RayCastCallback() {

            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if (fixture.getFilterData().categoryBits == gameManager.PLAYER_BIT || fixture.getFilterData().categoryBits == gameManager.POWERUP_BIT) {
                    return 0;
                }

                if (fraction < 1.0f) {
                    hit = true;
                }
                return 0;
            }
        };

        for (int i = 0; i < 3; i++) {
            Vector2 tmpV = new Vector2(set1);
            b2dWorld.rayCast(rayCastCallback, set, tmpV.add((1 - i) * 0.4f, 0));

        }
        return hit;
    }


    private boolean hitHorizontal(Body body, Vector2 set, Vector2 set1) {
        World b2dWorld = body.getWorld();
        hit = false;

        RayCastCallback rayCastCallback = new RayCastCallback() {

            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if (fixture.getFilterData().categoryBits == gameManager.PLAYER_BIT || fixture.getFilterData().categoryBits == gameManager.POWERUP_BIT) {
                    return 0;
                }

                if (fraction < 1.0f) {
                    hit = true;
                }
                return 0;
            }
        };

        for (int i = 0; i < 3; i++) {
            Vector2 tmpV = new Vector2(set1);
            b2dWorld.rayCast(rayCastCallback, set, tmpV.add(0, (1 - i) * 0.4f));

        }
        return hit;
    }

    private void changeWalkingState(Enemy enemy) {
        enemy.state = Enemy.State.getRandWalkingState();
    }
}
