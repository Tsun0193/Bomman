package com.bomman.game.listeners;

import com.badlogic.gdx.physics.box2d.*;

public class box2dListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
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
