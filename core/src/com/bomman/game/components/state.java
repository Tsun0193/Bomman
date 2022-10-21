package com.bomman.game.components;

import com.artemis.Component;

public class state extends Component {
    private String currentState;
    private float stateTime;

    public state() {

    }
    public state(String state){
        stateTime = 0;
        this.currentState = state;
    }

    public void resetStateTime() {
        stateTime = 0;
    }

    public String getCurrentState() {
        return this.currentState;
    }

    public void setCurrentState(String currentState) {
        if(!this.currentState.equals(currentState)){
            this.currentState = currentState;
            resetStateTime();
        }
    }

    public float getStateTime() {
        return this.stateTime;
    }

    public void addStateTime(float add) {
        this.stateTime += add;
    }
}
/* Final */