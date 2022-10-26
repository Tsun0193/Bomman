package com.bomman.game.components;

import com.artemis.Component;

public class buff extends Component {
    public enum Type {
        AMMO,
        POWER,
        SPEED,
        KICK,
        REMOTE,
        ONE_UP;

        public static Type getRandomType() {
            int index;
            int random = (int) (Math.random() * 10);
            if (random < 3) {
                index = 0;
            }
            else if (random < 5) {
                index = 1;
            }
            else if (random < 7) {
                index = 2;
            }
            else if (random < 8) {
                index = 3;
            }
            else if (random < 9) {
                index = 4;
            }
            else {
                index = 5;
            }
            return values()[index];
        }
    }

    public Type type;

    public float life;

    /**
     * Default Constructor.
     */
    public buff() {
        this(6.0f);
    }

    /**
     * Uni-variate Constructor.
     * @param life float
     */
    public buff(float life) {
        type = Type.getRandomType();
        this.life = life;
    }
}
/* FINAL */
