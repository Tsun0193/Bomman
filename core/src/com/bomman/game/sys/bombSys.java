package com.bomman.game.sys;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.bomman.game.builders.actorBuilder;
import com.bomman.game.components.bomb;
import com.bomman.game.components.rigidBody;
import com.bomman.game.components.state;
import com.bomman.game.components.transform;
import com.bomman.game.game.gameManager;

public class bombSys extends IteratingSystem {
    protected ComponentMapper<bomb> mBomb;
    protected ComponentMapper<rigidBody> mRigidBody;
    protected ComponentMapper<state> mState;

    private boolean movable;

    private final Vector2 fromV;
    private final Vector2 toV;

    public bombSys() {
        super(Aspect.all(bomb.class, rigidBody.class, transform.class, state.class));
        fromV = new Vector2();
        toV = new Vector2();
    }

    @Override
    protected void process(int entityId) {
        bomb Bomb = mBomb.get(entityId);
        state State = mState.get(entityId);
        rigidBody rBody = mRigidBody.get(entityId);

        Body body = rBody.body;

        Bomb.cd -= world.getDelta();

        if (Bomb.cd <= 0) {
            Bomb.state = bomb.State.EXPLODING;
        }

        switch (Bomb.state) {
            case EXPLODING:
                State.setCurrentState("exploding");
                gameManager.getInstance().playSound("Explosion.ogg", 1.0f, MathUtils.random(0.6f, 0.8f), 0);
                actorBuilder ActorBuilder = actorBuilder.init(body.getWorld(), world);
                ActorBuilder.createExplosion(body.getPosition().x, body.getPosition().y, Bomb.power);

                World box2DWorld = body.getWorld();
                box2DWorld.destroyBody(body);
                world.delete(entityId);
                break;
            case MOVING_UP:
                if (checkMovable(body, fromV.set(body.getPosition()), toV.set(body.getPosition().x, body.getPosition().y + 0.55f))) {
                    body.setLinearVelocity(0, Bomb.speed);
                } else {
                    body.setLinearVelocity(0, 0);
                    body.setTransform(MathUtils.floor(body.getPosition().x) + 0.5f, MathUtils.floor(body.getPosition().y) + 0.5f, 0);
                    Bomb.state = bomb.State.NORMAL;
                }
                break;
            case MOVING_DOWN:
                if (checkMovable(body, fromV.set(body.getPosition()), toV.set(body.getPosition().x, body.getPosition().y - 0.55f))) {
                    body.setLinearVelocity(0, -Bomb.speed);
                } else {
                    body.setLinearVelocity(0, 0);
                    body.setTransform(MathUtils.floor(body.getPosition().x) + 0.5f, MathUtils.floor(body.getPosition().y) + 0.5f, 0);
                    Bomb.state = bomb.State.NORMAL;
                }
                break;
            case MOVING_LEFT:
                if (checkMovable(body, fromV.set(body.getPosition()), toV.set(body.getPosition().x - 0.55f, body.getPosition().y))) {
                    body.setLinearVelocity(-Bomb.speed, 0);
                } else {
                    body.setLinearVelocity(0, 0);
                    body.setTransform(MathUtils.floor(body.getPosition().x) + 0.5f, MathUtils.floor(body.getPosition().y) + 0.5f, 0);
                    Bomb.state = bomb.State.NORMAL;
                }
                break;

            case MOVING_RIGHT:
                if (checkMovable(body, fromV.set(body.getPosition()), toV.set(body.getPosition().x + 0.55f, body.getPosition().y))) {
                    body.setLinearVelocity(Bomb.speed, 0);
                } else {
                    body.setLinearVelocity(0, 0);
                    body.setTransform(MathUtils.floor(body.getPosition().x) + 0.5f, MathUtils.floor(body.getPosition().y) + 0.5f, 0);
                    Bomb.state = bomb.State.NORMAL;
                }
                break;
            case NORMAL:
            default:
                State.setCurrentState("normal");
                break;
        }
    }

    private boolean checkMovable(Body body, Vector2 set, Vector2 set1) {
        World box2DWorld = body.getWorld();
        movable = true;

        RayCastCallback rayCastCallback = new RayCastCallback() {

            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if (fixture.getFilterData().categoryBits == gameManager.INDESTRUCTABLE_BIT
                        | fixture.getFilterData().categoryBits == gameManager.BREAKABLE_BIT
                        | fixture.getFilterData().categoryBits == gameManager.BOMB_BIT
                        | fixture.getFilterData().categoryBits == gameManager.ENEMY_BIT
                        | fixture.getFilterData().categoryBits == gameManager.PLAYER_BIT) {
                    movable = false;
                    return 0;
                }
                return 0;
            }
        };

        box2DWorld.rayCast(rayCastCallback, set, set1);
        return movable;
    }
}
