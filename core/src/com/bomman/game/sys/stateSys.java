package com.bomman.game.sys;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.bomman.game.components.state;

public class stateSys extends IteratingSystem {
    ComponentMapper<state> mState;

    public stateSys() {
        super(Aspect.all(state.class));
    }

    @Override
    protected void process(int entityId) {
        state State = mState.get(entityId);
        State.addStateTime(world.getDelta());
    }
}
