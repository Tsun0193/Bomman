package com.bomman.game.components;

import com.artemis.Component;
import com.bomman.game.game.gameManager;

public class Enemy extends Component {
    public static short defaultMaskBits = gameManager.INDESTRUCTABLE_BIT |
            gameManager.BREAKABLE_BIT |
            gameManager.PLAYER_BIT |
            gameManager.EXPLOSION_BIT;

    public enum State {
        moveUp,
        moveDown,
        moveLeft,
        moveRight,
        dead,
        attackDown,
        attackLeft,
        attackRight,
        attackUp,
        damaged;
        public static State getRandWalkingState() {
            return values()[(int)(Math.random()*4)];
        }
    }

    public State state;

    protected float speed;
    public int hp;
    public int receivedDmg;
    public float lifespan;

    private String sound;
    public String type;

    public Enemy() {
        
    }
    public Enemy(int hp) {
        this(hp, 2);
    }

    public Enemy(int hp, float speed) {
        this(hp, speed, "EnemyDie.ogg");
    }

    public Enemy(int hp, float speed, String sound) {
        this(hp, speed, sound, "basic");
    }

    public Enemy(int hp, float speed, String sound, String type) {
        state = State.getRandWalkingState();

        this.hp = hp;
        this.speed = speed;
        this.sound = sound;
        this.type = type;

        lifespan = 0;
        receivedDmg = 0;

        gameManager.enemiesLeft++;
    }

    public void damage(int damage) {
        hp -= damage;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public String getDieSound() {
        return sound;
    }

    public void setDieSound(String dieSound) {
        this.sound = dieSound;
    }

}
/* Final */


