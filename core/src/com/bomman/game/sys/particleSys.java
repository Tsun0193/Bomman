package com.bomman.game.sys;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bomman.game.components.particle;

public class particleSys extends IteratingSystem {
    protected ComponentMapper<particle> mParticle;

    private final SpriteBatch batch;

    public particleSys(SpriteBatch batch) {
        super(Aspect.all(particle.class));
        this.batch = batch;
    }

    @Override
    protected void begin() {
        batch.begin();
    }

    @Override
    protected void end() {
        batch.end();
    }

    @Override
    protected void process(int entityId) {
        particle Particle = mParticle.get(entityId);

        if (!Particle.particleEffect.isComplete()) {
            Particle.particleEffect.draw(batch, world.getDelta());
        } else {
            world.delete(entityId);
        }
    }
}
