package com.bomman.game.sys;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.bomman.game.components.explosion;
import com.bomman.game.components.rigidBody;
import com.bomman.game.components.state;

public class explosionSys extends IteratingSystem {
    protected ComponentMapper<explosion> mExplosion;
    protected ComponentMapper<rigidBody> mRigidBody;
    protected ComponentMapper<state> mState;

    public explosionSys() {
        super(Aspect.all(explosion.class, state.class));
    }

    @Override
    protected void process(int entityId) {
        rigidBody rBody = mRigidBody.get(entityId);
        state s = mState.get(entityId);
        if (s.getStateTime() > 0.75f) {
            rBody.body.getWorld().destroyBody(rBody.body);
            world.delete(entityId);
        }
    }
}
