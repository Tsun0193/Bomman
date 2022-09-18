package com.bomman.game.sys;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.Bag;
import com.artemis.utils.Sort;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bomman.game.components.renderer;
import com.bomman.game.components.transform;

import java.util.Comparator;

public class renderSys extends EntitySystem {
    private final SpriteBatch batch;
    protected ComponentMapper<renderer> mRenderer;
    protected ComponentMapper<transform> mTransform;

    /**
     * Constructor.
     *
     * @param spriteBatch batch
     */
    public renderSys(SpriteBatch spriteBatch) {
        super(Aspect.all(transform.class, renderer.class));
        this.batch = spriteBatch;
    }

    @Override
    protected void begin() {
        batch.begin();
    }

    @Override
    protected void processSystem() {
        Bag<Entity> bagE = getEntities();
        Sort sort = Sort.instance();
        sort.sort(bagE, new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                transform t1 = mTransform.get(o1);
                transform t2 = mTransform.get(o2);
                if (t1.temp < t2.temp) {
                    return 1;
                } else {
                    if (t1.temp > t2.temp) {
                        return -1;
                    } else return 0;
                }
            }
        });
        for (Entity e : bagE) {
            process(e);
        }
    }

    @Override
    protected void end() {
        batch.end();
    }

    /**
     * Process Method.
     *
     * @param e Entity
     */
    protected void process(Entity e) {
        transform t = mTransform.get(e);
        renderer r = mRenderer.get(e);

        r.setSpritePos(t.xPos, t.yPos);
        r.setSpriteRotation(t.degree);
        r.setSpriteScale(t.xScale, t.yScale);

        r.spriteDraw(this.batch);
    }
}
