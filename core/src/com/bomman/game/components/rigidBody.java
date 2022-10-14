package com.bomman.game.components;

import com.artemis.Component;
import com.badlogic.gdx.physics.box2d.Body;

public class rigidBody extends Component {
    public Body body;

    public rigidBody(Body body) {
        this.body = body;
    }
}
