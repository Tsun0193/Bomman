package com.bomman.game.builders;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;

public class worldBuilder {
    private final World box2DWorld;
    private final com.artemis.World world;
    private Sprite sprite;

    private int mapWidth;
    private int mapHeight;

    public worldBuilder(World box2DWorld, com.artemis.World world) {
        this.box2DWorld = box2DWorld;
        this.world = world;
        this.sprite = new Sprite();
    }

    public void build() {
        
    }

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
