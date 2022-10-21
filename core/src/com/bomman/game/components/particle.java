package com.bomman.game.components;

import com.artemis.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.bomman.game.game.gameManager;

public class particle extends Component {
    public ParticleEffect particleEffect;

    public particle() {

    }
    /**
     *  Multi-variate Constructor.
     */
    public particle(String particleFileString, float x, float y) {
        particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal(particleFileString), Gdx.files.internal("particles"));
        particleEffect.setPosition(x, y);
        particleEffect.scaleEffect(1 / gameManager.PPM);
        particleEffect.start();
    }
}
/* Final */
