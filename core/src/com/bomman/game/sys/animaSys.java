package com.bomman.game.sys;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.bomman.game.components.anima;
import com.bomman.game.components.renderer;
import com.bomman.game.components.state;

public class animaSys extends IteratingSystem {
    ComponentMapper<renderer> mapRenderer;
    ComponentMapper<anima> mapAnima;
    ComponentMapper<state> mapState;

    public animaSys() {
        super(Aspect.all(renderer.class, anima.class, state.class));
    }

    @Override
    protected void process(int id) {
        state State = mapState.get(id);
        anima Anim = mapAnima.get(id);
        renderer Renderer = mapRenderer.get(id);


        Renderer.setSpriteRegion(Anim.getTextureRegion(State.getCurrentState(), State.getStateTime()));

    }
}
