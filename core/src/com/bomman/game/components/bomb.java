package com.bomman.game.components;

import com.artemis.Component;
import com.bomman.game.game.gameManager;

public class bomb extends Component {
    public static short defaultMaskBits = gameManager.INDESTRUCTIIBLE_BIT |
            gameManager.BREAKABLE_BIT |
            gameManager.EXPLOSION_BIT;
    public enum State {
        normal,
        moveUp,
        moveDown,
        moveLeft,
        moveRight,
        explode
    }
    public State state;
    public float cd;
    public int power;
    public float speed;

    public bomb() {
        this(1, 2.0f);
    }

    public bomb(int power){
        this(power, 2.0f);
    }

    public bomb(int power, float cd) {
        this.power = power;
        this.cd = cd;
        this.speed = 6.0f;
        state = State.normal;
    }

    public void setMove(bomb.State state) {
        this.state = state;
    }

}
