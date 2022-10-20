package com.bomman.game.components;

import com.artemis.Component;

public class breakableObj extends Component {
    public enum State {
        EXPLODING,
        NORMAL,
    }
    public State state;

    public breakableObj(){
         state = State.NORMAL;
    }
}
/* FINAL */
