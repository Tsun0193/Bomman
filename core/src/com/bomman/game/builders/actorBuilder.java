package com.bomman.game.builders;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.bomman.game.game.gameManager;

public class actorBuilder {
    private static final actorBuilder instance = new actorBuilder();
    protected final float radius = 0.45f;
    private World box2DWorld;
    private com.artemis.World world;
    private AssetManager assetManager;
    private final Vector2 fromV = new Vector2();
    private final Vector2 toV = new Vector2();
    private actorBuilder() {};

    public static actorBuilder init(World box2DWorld, com.artemis.World world){
        instance.box2DWorld = box2DWorld;
        instance.assetManager = gameManager.getInstance().getAssetManager();
        instance.world = world;
        return instance;
    }

    public void createWall() {}
}
