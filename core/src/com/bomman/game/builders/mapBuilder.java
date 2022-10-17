package com.bomman.game.builders;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.bomman.game.game.gameManager;

public class mapBuilder {
    public static enum Block {
        EMPTY(255, 255, 255),
        WALL(0, 0, 0),
        BREAKABLE(0, 255, 255),
        INDESTRUCABLE(0, 0, 255),
        PLAYER(255, 0, 0),
        ENEMY1(0, 255, 0),
        ENEMY2(0, 128, 0);

        final int color;

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

    public void loadMap() {
        actorBuilder builder = actorBuilder.init(box2DWorld, world);
        int colorCode;

        for (int i = 0; i < mapH; i++) {
            for (int j = 0; j < mapW; j++) {
                colorCode = pixmap.getPixel(j, mapH - i - 1);
                if (Block.WALL.identical(colorCode)) {
                    builder.createWall(j + 0.5f, i + 0.5f, mapW, mapH, tileTextureAtlas);
                } else if (Block.BREAKABLE.identical(colorCode)) {
                    builder.createBreakableObj(j + 0.5f, i + 0.5f, tileTextureAtlas);
                } else if (Block.INDESTRUCABLE.identical(colorCode)) {
                    builder.createIndestructible(j + 0.5f, i + 0.5f, tileTextureAtlas);
                } else if (Block.PLAYER.identical(colorCode)) {
                    builder.createPlayer(j + 0.5f, i + 0.5f, false);
                    gameManager.getInstance().setPlayerResPos(new Vector2(j + 0.5f, i + 0.5f));
                } else if (Block.ENEMY1.identical(colorCode)) {
                    switch (level) {
                        case 5:
                            builder.createBoss1(j + 0.5f, i + 0.5f);
                            break;
                        case 4:
                            builder.createBombEnemy(j + 0.5f, i + 0.5f);
                            break;
                        case 3:
                            builder.createHare(j + 0.5f, i + 0.5f);
                            break;
                        case 1:
                        default:
                            builder.createOctopus(j + 0.5f, i + 0.5f);
                            break;
                    }
                } else if (Block.ENEMY2.identical(colorCode)) {
                    switch (level) {
                        case 5:
                            break;
                        case 4:
                            builder.createHare(j + 0.5f, i + 0.5f);
                            break;
                        case 3:
                            builder.createHare(j + 0.5f, i + 0.5f);
                            break;
                        case 2:
                            builder.createSlime(j + 0.5f, i + 0.5f);
                            break;
                        case 1:
                        default:
                            builder.createOctopus(j + 0.5f, i + 0.5f);
                            break;
                    }
                }
            }
            gameManager.getInstance().setPlayerGoalPos(new Vector2(mapW / 2, mapH / 2));
        }
    }

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
        sprite.setBounds(0, 0, 1, 1);
        return sprite;
    }
}
