package com.bomman.game.builders;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.bomman.game.game.gameManager;

public class mapBuilder {
    public static enum Block {
        EMPTY(255,255,255),
        WALL(0,0,0),
        BREAKABLE(0,255,255),
        INDESTRUCABLE(0,0,255),
        PLAYER(255,0,0),
        ENEMY1(0,255,0),
        ENEMY2(0,128,0);

        int color;

        Block(int red, int green, int blue) {
            color = red << 24 |
                    green << 16 |
                    blue << 8 |
                    0xff;
        }

        boolean identical(int color) {
            return this.color == color;
        }
    }
    protected final World box2DWorld;
    protected final com.artemis.World world;
    protected int level;
    protected int mapW;
    protected int mapH;
    protected AssetManager assetManager;
    protected Pixmap pixmap;
    protected TextureAtlas tileTextureAtlas;

    public mapBuilder(World box2DWorld, com.artemis.World world, int lv) {
        this.box2DWorld = box2DWorld;
        this.world = world;
        this.level = lv;
        assetManager = gameManager.getInstance().getAssetManager();

        pixmap = assetManager.get("maps/level_" + level + ".png", Pixmap.class);
        switch (level) {
            default:
                tileTextureAtlas = assetManager.get("maps/area_1_tiles.pack", TextureAtlas.class);
                break;
            case 1:
            case 2:
            case 3:
                tileTextureAtlas = assetManager.get("maps/area_2_tiles.pack", TextureAtlas.class);
                break;
            case 4:
            case 5:
                tileTextureAtlas = assetManager.get("maps/area_3_tiles.pack", TextureAtlas.class);
                break;
        }

        mapW = pixmap.getWidth();
        mapH = pixmap.getHeight();
    }

    public void loadMap() {}

    public int getMapH() {
        return mapH;
    }

    public int getMapW() {
        return mapW;
    }

    protected Sprite createSprite() {
        TextureRegion textureRegion = tileTextureAtlas.findRegion("ground");
        Sprite sprite = new Sprite();

        sprite.setRegion(textureRegion);
        sprite.setBounds(0,0,1,1);
        return sprite;
    }
}
