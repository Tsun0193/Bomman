package com.bomman.game.sys;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.bomman.game.components.*;

public class playerSys extends IteratingSystem {
    protected ComponentMapper<character> mapCharacter;
    protected ComponentMapper<rigidBody> mapRigidBody;
    protected ComponentMapper<state> mapState;
    protected ComponentMapper<renderer> mapRenderer;

    private boolean hitting;
    private boolean kicking;
//    private Bomb kickingBomb;
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
    }
}
