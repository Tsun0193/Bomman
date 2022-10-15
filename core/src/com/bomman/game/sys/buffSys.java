package com.bomman.game.sys;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.bomman.game.components.buff;
import com.bomman.game.components.renderer;
import com.bomman.game.components.rigidBody;
import com.bomman.game.components.state;

public class buffSys extends IteratingSystem {
    protected ComponentMapper<buff> mPowerUp;
    protected ComponentMapper<rigidBody> mRigidBody;
    protected ComponentMapper<renderer> mRenderer;
    protected ComponentMapper<state> mState;

    public buffSys() {
        super(Aspect.all(buff.class, rigidBody.class, renderer.class, state.class));
    }

    @Override
    protected void process(int entityId) {
        buff Buff = mPowerUp.get(entityId);
        rigidBody rBody = mRigidBody.get(entityId);
        renderer Renderer = mRenderer.get(entityId);
        state State = mState.get(entityId);

        if (State.getStateTime() > Buff.life - 2.0f) {
            Renderer.setSpriteColor(new Color(1.0f, 1.0f, 1.0f, 1.0f - MathUtils.sin(State.getStateTime() * 20)));
        }

        if (State.getStateTime() > Buff.life) {
            rBody.body.getWorld().destroyBody(rBody.body);
            world.delete(entityId);
        }
    }
}
