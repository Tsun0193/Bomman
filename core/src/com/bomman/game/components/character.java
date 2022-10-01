package com.bomman.game.components;

import com.artemis.Component;
import com.bomman.game.game.gameManager;

public class character extends Component {
    public enum State {
        dead,
        idleDown,
        idleLeft,
        idleRight,
        idleUp,
        moveDown,
        moveLeft,
        moveRight,
        moveUp,
        special
    }
    public State state;
    public static short defaultMaskBits = gameManager.INDESTRUCTABLE_BIT | gameManager.BREAKABLE_BIT | gameManager.ENEMY_BIT | gameManager.BOMB_BIT | gameManager.EXPLOSION_BIT | gameManager.POWERUP_BIT | gameManager.PORTAL_BIT;
    public static short invincibleMaskBit = gameManager.INDESTRUCTABLE_BIT | gameManager.BREAKABLE_BIT | gameManager.POWERUP_BIT | gameManager.PORTAL_BIT;
    public float maxSpeed;
    public int hp;
    public int bombPower;
    public int bombCapacity;
    public static final int MAX_BOMB_CAPACITY = 10;
    public static final int MAX_BOMB_POWER = 6;
    public int bombRemaining;
    public float bombRegenerateTime;

    public float bombRegenerateTimeLeft;

    public boolean godmode;
    public float godTimer;
    public int receivedDmg;

    public character(boolean resetPlayerAttributes) {
        state = State.idleRight;

        if (resetPlayerAttributes) {
            gameManager.resetPlayerAttributes();
        }

        this.hp = 3;
        this.bombCapacity = gameManager.playerBombCount;
        this.bombRemaining = 0;
        this.bombPower = gameManager.playerBombPow;
        this.godmode = true;
        this.godTimer = 3.0f;
        this.bombRegenerateTime = gameManager.playerBombGenerateTime;
        this.bombRegenerateTimeLeft = 0.0f;
        this.maxSpeed = 5.0f + gameManager.playerMaxSpeed * 1.25f;
        this.receivedDmg = 0;
    }

    public void damage(int dmg) {
        if(!godmode) {
            this.hp -= dmg;
        }
    }

    public void addAmmo() {
        this.bombCapacity++;
        gameManager.playerBombCount = Math.max(bombCapacity, gameManager.playerBombCount);
    }
}


