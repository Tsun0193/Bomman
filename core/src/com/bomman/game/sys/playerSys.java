package com.bomman.game.sys;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.bomman.game.builders.actorBuilder;
import com.bomman.game.components.*;
import com.bomman.game.game.gameManager;

import java.util.Queue;

public class playerSys extends IteratingSystem {
    protected ComponentMapper<character> mapCharacter;
    protected ComponentMapper<rigidBody> mapRigidBody;
    protected ComponentMapper<state> mapState;
    protected ComponentMapper<renderer> mapRenderer;

    private boolean hitting;
    private boolean kicking;
    private bomb kickedBomb;
    private final Vector2 fromV;
    private final Vector2 toV;

    public playerSys() {
        super(Aspect.all(character.class, transform.class, renderer.class, rigidBody.class, state.class));
        fromV = new Vector2();
        toV = new Vector2();
    }

    @Override
    protected void process(int entityId) {
        character Character = mapCharacter.get(entityId);
        rigidBody RigidBody = mapRigidBody.get(entityId);
        state State = mapState.get(entityId);
        renderer Renderer = mapRenderer.get(entityId);

        Body body = RigidBody.body;

        Vector2 linearVelocity = body.getLinearVelocity();

        float maxSpeed = Character.maxSpeed;

        if (Character.hp > 0 && Character.state != character.State.teleport) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                Character.powerUpAmmo();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                Character.powerUpPower();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
                Character.powerUpSpeed();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
                Character.powerUpKick();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
                Character.powerUpRemote();
            }



            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                if (Character.godmode || !hitBombVertical(body, fromV.set(body.getPosition()), toV.set(body.getPosition().x, body.getPosition().y + 0.5f))) {
                    if (Math.abs(linearVelocity.y) < maxSpeed) {
                        body.applyLinearImpulse(new Vector2(0, Character.acceleration * body.getMass()), body.getWorldCenter(), true);
                    }
                }

                Character.state = character.State.moveUp;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                if (Character.godmode || !hitBombVertical(body, fromV.set(body.getPosition()), toV.set(body.getPosition().x, body.getPosition().y - 0.5f))) {
                    if (Math.abs(linearVelocity.y) < maxSpeed) {
                        body.applyLinearImpulse(new Vector2(0, -Character.acceleration * body.getMass()), body.getWorldCenter(), true);
                    }
                }

                Character.state = character.State.moveDown;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                if (Character.godmode || !hitBombHorizontal(body, fromV.set(body.getPosition()), toV.set(body.getPosition().x - 0.5f, body.getPosition().y))) {
                    if (Math.abs(linearVelocity.x) < maxSpeed) {
                        body.applyLinearImpulse(new Vector2(-Character.acceleration * body.getMass(), 0), body.getWorldCenter(), true);
                    }
                }

                Character.state = character.State.moveLeft;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                if (Character.godmode || !hitBombHorizontal(body, fromV.set(body.getPosition()), toV.set(body.getPosition().x + 0.5f, body.getPosition().y))) {
                    if (Math.abs(linearVelocity.x) < maxSpeed) {
                        body.applyLinearImpulse(new Vector2(Character.acceleration * body.getMass(), 0), body.getWorldCenter(), true);
                    }
                }

                Character.state = character.State.moveRight;
            }

            // set bomb or kick bomb
            if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                kicking = false;
                if (Character.kickBomb) {
                    switch (Character.state) {
                        case moveUp:
                            if (checkCanKickBomb(body, fromV.set(body.getPosition()), toV.set(new Vector2(body.getPosition().x, body.getPosition().y + 0.6f)))) {
                                kickedBomb.setMove(bomb.State.moveUp);
                            }
                            break;
                        case moveDown:
                            if (checkCanKickBomb(body, fromV.set(body.getPosition()), toV.set(new Vector2(body.getPosition().x, body.getPosition().y - 0.6f)))) {
                                kickedBomb.setMove(bomb.State.moveDown);
                            }
                            break;
                        case moveLeft:
                            if (checkCanKickBomb(body, fromV.set(body.getPosition()), toV.set(new Vector2(body.getPosition().x - 0.6f, body.getPosition().y)))) {
                                kickedBomb.setMove(bomb.State.moveLeft);
                            }
                            break;
                        case moveRight:
                            if (checkCanKickBomb(body, fromV.set(body.getPosition()), toV.set(new Vector2(body.getPosition().x + 0.6f, body.getPosition().y)))) {
                                kickedBomb.setMove(bomb.State.moveRight);
                            }
                            break;
                        default:
                            break;

                    }
                    gameManager.getInstance().playSound("KickBomb.ogg");
                }

                if (!kicking && Character.bombRemaining > 0) {
                    actorBuilder ActorBuilder = actorBuilder.init(body.getWorld(), world);

                    if (Character.remoteBomb) {
                        gameManager.getInstance().getRemoteBombQueue().offer(
                                ActorBuilder.createRemoteBomb(Character , body.getPosition().x, body.getPosition().y)
                        );
                    } else {
                        ActorBuilder.createBomb(Character, body.getPosition().x, body.getPosition().y);
                    }
                    Character.bombRemaining--;
                    gameManager.getInstance().playSound("PlaceBomb.ogg");
                }

            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.Z) && Character.remoteBomb) {
                Queue<Entity> remoteBombQueue = gameManager.getInstance().getRemoteBombQueue();
                while (!remoteBombQueue.isEmpty() && remoteBombQueue.peek().getComponent(bomb.class) == null) {
                    remoteBombQueue.remove();
                }

                Entity remoteBombEntity = remoteBombQueue.poll();
                if (remoteBombEntity != null) {
                    bomb remoteBomb = remoteBombEntity.getComponent(bomb.class);
                    remoteBomb.cd = 0;
                }
            }

            if (Character.bombRemaining < Character.bombCapacity) {
                Character.bombRegenerateTimeLeft -= world.getDelta();
            }
            if (Character.bombRegenerateTimeLeft <= 0) {
                Character.bombRemaining++;
                Character.bombRegenerateTimeLeft = Character.bombRegenerateTime;
            }
        }

        gameManager.playerBombRemaining = Character.bombRemaining;
        gameManager.playerBombGenerateTimeLeft = Character.bombRegenerateTimeLeft;

        if (linearVelocity.len2() < 0.1f) {
            switch (Character.state) {
                case moveUp:
                    Character.state = character.State.idleUp;
                    break;
                case moveDown:
                    Character.state = character.State.idleDown;
                    break;
                case moveLeft:
                    Character.state = character.State.idleLeft;
                    break;
                case moveRight:
                    Character.state = character.State.idleRight;
                    break;
                default:
                    break;
            }
        }

        Character.godTimer -= world.getDelta();
        if (Character.godTimer < 0) {
            Character.godmode = false;
        }

        if (Character.godmode) {
            Filter filter = body.getFixtureList().get(0).getFilterData();
            filter.maskBits = character.invincibleMaskBit;
            body.getFixtureList().get(0).setFilterData(filter);
            Renderer.setSpriteColor(new Color(1, 1, 1, 1.2f + MathUtils.sin(Character.godTimer * 24)));
        } else {
            Filter filter = body.getFixtureList().get(0).getFilterData();
            filter.maskBits = character.defaultMaskBits;
            body.getFixtureList().get(0).setFilterData(filter);
            Renderer.setSpriteColor(Color.WHITE);
        }

        if (Character.receivedDmg > 0) {
            Character.damage(Character.receivedDmg);
            Character.receivedDmg = 0;
        }

        if (Character.hp <= 0) {
            Character.state = character.State.dead;
        }

        switch (Character.state) {
            case dead:
                State.setCurrentState("dead");
                Filter filter = body.getFixtureList().get(0).getFilterData();
                filter.maskBits = gameManager.NOTHING_BIT;
                body.getFixtureList().get(0).setFilterData(filter);

                if (State.getStateTime() <= 0) {
                    gameManager.getInstance().playSound("Die.ogg");
                }

                if (State.getStateTime() > 0.65f) {
                    World b2dWorld = body.getWorld();
                    b2dWorld.destroyBody(body);
                    world.delete(entityId);

                    gameManager.playerLives--;
                    if (!gameManager.infLives && gameManager.playerLives < 1) {
                        gameManager.gameOver = true;
                    } else {
                        actorBuilder ActorBuilder = actorBuilder.init(b2dWorld, world);
                        Vector2 respawnPosition = gameManager.getInstance().getPlayerResPos();
                        ActorBuilder.createPlayer(respawnPosition.x, respawnPosition.y, gameManager.reset);
                    }
                }
                break;
            case teleport:
                State.setCurrentState("teleport");
                break;
            case moveUp:
                State.setCurrentState("moveUp");
                break;
            case moveLeft:
                State.setCurrentState("moveLeft");
                break;
            case moveDown:
                State.setCurrentState("moveDown");
                break;
            case moveRight:
                State.setCurrentState("moveRight");
                break;
            case idleLeft:
                State.setCurrentState("idleLeft");
                break;
            case idleRight:
                State.setCurrentState("idleRight");
                break;
            case idleUp:
                State.setCurrentState("idleUp");
                break;
            case idleDown:
            default:
                State.setCurrentState("idleDown");
                break;
        }
    }

    private boolean checkCanKickBomb(Body body, Vector2 set, Vector2 set1) {
        World b2dWorld = body.getWorld();
        kickedBomb = null;
        kicking = false;

        RayCastCallback rayCastCallback = new RayCastCallback() {

            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if (fixture.getFilterData().categoryBits == gameManager.BOMB_BIT) {
                    Entity bombEntity = (Entity) fixture.getBody().getUserData();
                    kickedBomb = bombEntity.getComponent(bomb.class);
                    return 0;
                }
                return 0;
            }
        };

        b2dWorld.rayCast(rayCastCallback, fromV, toV);
        if (kickedBomb != null) {
            kicking = true;
        }
        return kicking;
    }

    private boolean hitBombHorizontal(final Body body, Vector2 set, Vector2 set1) {
        World box2DWorld = body.getWorld();
        hitting = false;

        RayCastCallback rayCastCallback = new RayCastCallback() {

            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if (fixture.getBody() == body) {
                    return 1;
                }

                if (fraction < 1.0f && fixture.getFilterData().categoryBits == gameManager.BOMB_BIT) {
                    hitting = true;
                }
                return 0;
            }
        };

        for (int i = 0; i < 3; i++) {
            Vector2 tmpV = new Vector2(toV);
            box2DWorld.rayCast(rayCastCallback, fromV, tmpV.add(0, (1 - i) * 0.4f));
        }
        return hitting;
    }

    protected boolean hitBombVertical(final Body body, Vector2 fromV, Vector2 toV) {
        World box2DWorld = body.getWorld();
        hitting = false;

        RayCastCallback rayCastCallback = new RayCastCallback() {

            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if (fixture.getBody() == body) {
                    return 1;
                }

                if (fraction < 1.0f && fixture.getFilterData().categoryBits == gameManager.BOMB_BIT) {
                    hitting = true;
                }
                return 0;
            }
        };

        for (int i = 0; i <= 2; i++) {
            Vector2 tmpV = new Vector2(toV);
            box2DWorld.rayCast(rayCastCallback, fromV, tmpV.add((1 - i) * 0.4f, 0));

        }
        return hitting;
    }
}
