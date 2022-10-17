package com.bomman.game.builders;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;

public class worldBuilder {
    private final World box2DWorld;
    private final com.artemis.World world;
    private Sprite sprite;

    private int mapWidth;
    private int mapHeight;

    /**
     * Constructor.
     * @param box2DWorld Mechanics
     * @param world Controller
     */
    public worldBuilder(World box2DWorld, com.artemis.World world) {
        this.box2DWorld = box2DWorld;
        this.world = world;
    }

    /**
     * Creating Builder + Load Map.
     * @param lv level
     */
    public void build(int lv) {
        mapBuilder mB;
        mB = new mapBuilder(box2DWorld, world, lv);
        mB.loadMap();
        sprite = mB.createSprite();
        mapWidth = mB.getMapW();
        mapHeight = mB.getMapH();
    }

    /**
     * <p>
     *     Getters.
     * </p>
     */
    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public Sprite getSprite() {
        return sprite;
    }
}
