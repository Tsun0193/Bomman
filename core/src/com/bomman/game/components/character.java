package com.bomman.game.components;

import com.artemis.Component;
import com.badlogic.gdx.math.MathUtils;
import com.bomman.game.game.gameManager;

public class character extends Component {

    public float acceleration;

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
        teleport
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
    public boolean kickBomb;
    public boolean remoteBomb;
    public float godTimer;
    public int receivedDmg;

    public character() {

    }

    /**
     * Uni-variate Constructor.
     * @param resetPlayerAttributes boolean
     */
    public character(boolean resetPlayerAttributes) {
        state = State.idleRight;

        if (resetPlayerAttributes) {
            gameManager.resetPlayerAttributes();
        }
        this.remoteBomb = gameManager.playerRemoteBomb;
        this.kickBomb = gameManager.playerKickBomb;
        this.hp = 1;
        this.bombCapacity = gameManager.playerBombCount;
        this.bombRemaining = 0;
        this.bombPower = gameManager.playerBombPow;
        this.godmode = true;
        this.godTimer = 3.0f;
        this.bombRegenerateTime = gameManager.playerBombGenerateTime;
        this.bombRegenerateTimeLeft = 0.0f;
        this.maxSpeed = 3.0f + gameManager.playerMaxSpeed * 1.2f;
        this.receivedDmg = 0;
        this.acceleration = 0.8f;
    }

    public void damage(int dmg) {
        if(!godmode) {
            this.hp -= dmg;
        }
    }

    public void powerUpAmmo() {
        if (bombCapacity < MAX_BOMB_CAPACITY) {
            bombCapacity++;
            gameManager.playerBombCount = bombCapacity;
        } else {
            decreaseBombRegeneratingTime();
        }

        gameManager.getInstance().playSound("Powerup.ogg");
    }

    public void powerUpPower() {
        if (bombPower < MAX_BOMB_POWER) {
            gameManager.playerBombPow++;
            bombPower = gameManager.playerBombPow;
        } else {
            decreaseBombRegeneratingTime();
        }

        gameManager.getInstance().playSound("Powerup.ogg");
    }

    public void powerUpSpeed() {
        if (maxSpeed <= 8.0f) {
            gameManager.playerMaxSpeed++;
            maxSpeed = 3.0f + gameManager.playerMaxSpeed * 1.2f;
        } else {
            decreaseBombRegeneratingTime();
        }

        gameManager.getInstance().playSound("Powerup.ogg");
    }

    public void powerUpKick() {
        if (!kickBomb) {
            kickBomb = true;
            gameManager.playerKickBomb = kickBomb;
        } else {
            decreaseBombRegeneratingTime();
        }

        gameManager.getInstance().playSound("Powerup.ogg");
    }

    public void powerUpRemote() {
        if (!remoteBomb) {
            remoteBomb = true;
            gameManager.playerRemoteBomb = remoteBomb;
        } else {
            decreaseBombRegeneratingTime();
        }

        gameManager.getInstance().playSound("Powerup.ogg");
    }

    public void decreaseBombRegeneratingTime() {
        if (bombRegenerateTime <= 0.2f) {
            return;
        }

        bombRegenerateTime -= 0.2f;
        gameManager.playerBombGenerateTime = bombRegenerateTime;
        bombRegenerateTimeLeft = MathUtils.clamp(bombRegenerateTimeLeft, 0, bombRegenerateTime);
    }
}
/* Final */


