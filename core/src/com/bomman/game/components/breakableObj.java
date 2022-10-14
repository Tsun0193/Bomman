package com.bomman.game.components;

import com.artemis.Component;

public class breakableObj extends Component {
    public enum State {
        explode,
        stable,
    }
    public State state;

    public breakableObj(){
         state = State.stable;
    }
}
