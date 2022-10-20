package com.bomman.game.sys;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.bomman.game.builders.actorBuilder;
import com.bomman.game.components.breakableObj;
import com.bomman.game.components.rigidBody;
import com.bomman.game.components.state;

public class breakObjSys extends IteratingSystem {
    protected ComponentMapper<breakableObj> mBreakable;
    protected ComponentMapper<state> mState;
    protected ComponentMapper<rigidBody> mRigidBody;

    public breakObjSys() {
        super(Aspect.all(breakableObj.class, state.class));
    }

    @Override
    protected void process(int entityId) {
        breakableObj obj = mBreakable.get(entityId);
        state State = mState.get(entityId);
        rigidBody rBody = mRigidBody.get(entityId);
        Body body = rBody.body;

        switch (obj.state) {
            case EXPLODING:
                State.setCurrentState("exploding");
                if (State.getStateTime() > 0.6f) {
                    body.getWorld().destroyBody(body);
                    world.delete(entityId);
                    if (Math.random() < 0.2) {
                        actorBuilder builder = actorBuilder.init(body.getWorld(), world);
                        builder.createBuff(body.getPosition().x, body.getPosition().y);
                    }
                }
                break;
            case NORMAL:
            default:
                State.setCurrentState("normal");
                break;
        }
    }
}
