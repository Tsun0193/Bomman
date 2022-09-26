package com.bomman.game.listeners;

import com.artemis.Entity;
import com.badlogic.gdx.physics.box2d.*;
import com.bomman.game.components.Enemy;
import com.bomman.game.components.character;
import com.bomman.game.game.gameManager;

public class box2dListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getFilterData().categoryBits == gameManager.EXPLOSION_BIT || fixtureB.getFilterData().categoryBits == gameManager.EXPLOSION_BIT) {
            if (fixtureA.getFilterData().categoryBits == gameManager.PLAYER_BIT) {
                Entity e = (Entity) fixtureA.getBody().getUserData();
                character player = e.getComponent(character.class);
                player.receivedDmg++;
            } else if (fixtureB.getFilterData().categoryBits == gameManager.PLAYER_BIT) {
                Entity e = (Entity) fixtureB.getBody().getUserData();
                character player = e.getComponent(character.class);
                player.receivedDmg++;
            } else if (fixtureA.getFilterData().categoryBits == gameManager.ENEMY_BIT) {
                Entity e = (Entity) fixtureA.getBody().getUserData();
                Enemy enemy = e.getComponent(Enemy.class);
                enemy.receivedDmg++;
            } else if (fixtureB.getFilterData().categoryBits == gameManager.ENEMY_BIT) {
                Entity e = (Entity) fixtureB.getBody().getUserData();
                Enemy enemy = e.getComponent(Enemy.class);
                enemy.receivedDmg++;
            }
//            } else if (fixtureA.getFilterData().categoryBits == gameManager.BOMB_BIT) {
//                Entity e = (Entity) fixtureA.getBody().getUserData();
//                Bomb bomb = e.getComponent(Bomb.class);
//                bomb.countDown = 0;
//            } else if (fixtureB.getFilterData().categoryBits == gameManager.BOMB_BIT) {
//                Entity e = (Entity) fixtureB.getBody().getUserData();
//                Bomb bomb = e.getComponent(Bomb.class);
//                bomb.countDown = 0;
//            } else if (fixtureA.getFilterData().categoryBits == gameManager.BREAKABLE_BIT) {
//                Entity e = (Entity) fixtureA.getBody().getUserData();
//                Breakable breakable = e.getComponent(Breakable.class);
//                breakable.state = Breakable.State.EXPLODING;
//            } else if (fixtureB.getFilterData().categoryBits == gameManager.BREAKABLE_BIT) {
//                Entity e = (Entity) fixtureB.getBody().getUserData();
//                Breakable breakable = e.getComponent(Breakable.class);
//                breakable.state = Breakable.State.EXPLODING;
//            }
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {
    }

    public void postSolve(Contact contact, ContactImpulse contactImpulse){
    }
}
