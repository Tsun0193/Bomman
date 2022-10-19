package com.bomman.game.listeners;

import com.artemis.Entity;
import com.badlogic.gdx.physics.box2d.*;
import com.bomman.game.components.*;
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
            } else if (fixtureA.getFilterData().categoryBits == gameManager.BOMB_BIT) {
                Entity e = (Entity) fixtureA.getBody().getUserData();
                bomb b = e.getComponent(bomb.class);
                b.cd = 0;
            } else if (fixtureB.getFilterData().categoryBits == gameManager.BOMB_BIT) {
                Entity e = (Entity) fixtureB.getBody().getUserData();
                bomb b = e.getComponent(bomb.class);
                b.cd = 0;
            } else if (fixtureA.getFilterData().categoryBits == gameManager.BREAKABLE_BIT) {
                Entity e = (Entity) fixtureA.getBody().getUserData();
                breakableObj obj = e.getComponent(breakableObj.class);
                obj.state = breakableObj.State.explode;
            } else if (fixtureB.getFilterData().categoryBits == gameManager.BREAKABLE_BIT) {
                Entity e = (Entity) fixtureB.getBody().getUserData();
                breakableObj obj = e.getComponent(breakableObj.class);
                obj.state = breakableObj.State.explode;
            }
        }
        else if (fixtureA.getFilterData().categoryBits == gameManager.ENEMY_BIT || fixtureB.getFilterData().categoryBits == gameManager.ENEMY_BIT) {
            if (fixtureA.getFilterData().categoryBits == gameManager.PLAYER_BIT) {
                Entity e = (Entity) fixtureA.getBody().getUserData();
                character player = e.getComponent(character.class);
                player.damage(1);
            } else if (fixtureB.getFilterData().categoryBits == gameManager.PLAYER_BIT) {
                Entity e = (Entity) fixtureB.getBody().getUserData();
                character player = e.getComponent(character.class);
                player.damage(1);
            }
        }
        else if (fixtureA.getFilterData().categoryBits == gameManager.PLAYER_BIT || fixtureB.getFilterData().categoryBits == gameManager.PLAYER_BIT) {
            if (fixtureA.getFilterData().categoryBits == gameManager.POWERUP_BIT) {
                Entity entity = (Entity) fixtureA.getBody().getUserData();
                buff Buff = entity.getComponent(buff.class);
                Buff.life = 0;
                Entity playerEntity = (Entity) fixtureB.getBody().getUserData();
                character player = playerEntity.getComponent(character.class);
                switch (Buff.type) {
                    case ONE_UP:
                        gameManager.getInstance().addLive();
                        break;
                    case REMOTE:
                        player.powerUpRemote();
                        break;
                    case KICK:
                        player.powerUpKick();
                        break;
                    case SPEED:
                        player.powerUpSpeed();
                        break;
                    case POWER:
                        player.powerUpPower();
                        break;
                    case AMMO:
                    default:
                        player.powerUpAmmo();
                        break;
                }
            } else if (fixtureB.getFilterData().categoryBits == gameManager.POWERUP_BIT) {
                Entity entity = (Entity) fixtureB.getBody().getUserData();
                buff Buff = entity.getComponent(buff.class);
                Buff.life = 0;
                Entity playerEntity = (Entity) fixtureA.getBody().getUserData();
                character player = playerEntity.getComponent(character.class);
                switch (Buff.type) {
                    case REMOTE:
                        player.powerUpRemote();
                        break;
                    case KICK:
                        player.powerUpKick();
                        break;
                    case SPEED:
                        player.powerUpSpeed();
                        break;
                    case POWER:
                        player.powerUpPower();
                        break;
                    case AMMO:
                    default:
                        player.powerUpAmmo();
                        break;
                }
            } else if (fixtureA.getFilterData().categoryBits == gameManager.PORTAL_BIT) {
                gameManager.gameFinished = true;
                Entity playerEntity = (Entity) fixtureB.getBody().getUserData();
                character player = playerEntity.getComponent(character.class);
                player.state = character.State.teleport;
            } else if (fixtureB.getFilterData().categoryBits == gameManager.PORTAL_BIT) {
                gameManager.gameFinished = true;
                Entity playerEntity = (Entity) fixtureA.getBody().getUserData();
                character player = playerEntity.getComponent(character.class);
                player.state = character.State.teleport;
            }
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
