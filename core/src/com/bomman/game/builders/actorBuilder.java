package com.bomman.game.builders;

import com.artemis.Entity;
import com.artemis.utils.EntityBuilder;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.bomman.game.components.*;
import com.bomman.game.game.gameManager;

import java.util.HashMap;

public class actorBuilder {
    private static final actorBuilder instance = new actorBuilder();
    protected final float radius = 0.45f;
    private World box2DWorld;
    private com.artemis.World world;
    private AssetManager assetManager;
    private final Vector2 fromV = new Vector2();
    private final Vector2 toV = new Vector2();

    private actorBuilder() {
    }

    public static actorBuilder init(World box2DWorld, com.artemis.World world) {
        instance.box2DWorld = box2DWorld;
        instance.assetManager = gameManager.getInstance().getAssetManager();
        instance.world = world;
        return instance;
    }

    public void createWall(float x, float y, float mapW, float mapH, TextureAtlas textureAtlas) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        Body body = box2DWorld.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = gameManager.INDESTRUCTABLE_BIT;
        fixtureDef.filter.maskBits = gameManager.PLAYER_BIT | gameManager.ENEMY_BIT | gameManager.BOMB_BIT;
        body.createFixture(fixtureDef);

        shape.dispose();
        renderer Renderer;


        if (x < 1.0f) {
            if (y < 1.0f) {
                Renderer = new renderer(new TextureRegion(textureAtlas.findRegion("wall"), 0, 16 * 2, 16, 16), 16 / gameManager.PPM, 16 / gameManager.PPM);
            } else if (y > mapH - 1) {
                Renderer = new renderer(new TextureRegion(textureAtlas.findRegion("wall"), 0, 16 * 0, 16, 16), 16 / gameManager.PPM, 16 / gameManager.PPM);

            } else {
                Renderer = new renderer(new TextureRegion(textureAtlas.findRegion("wall"), 0, 16 * 1, 16, 16), 16 / gameManager.PPM, 16 / gameManager.PPM);

            }
        } else if (x > mapW - 1) {
            if (y < 1.0f) {
                Renderer = new renderer(new TextureRegion(textureAtlas.findRegion("wall"), 16 * 2, 16 * 2, 16, 16), 16 / gameManager.PPM, 16 / gameManager.PPM);

            } else if (y > mapH - 1) {
                Renderer = new renderer(new TextureRegion(textureAtlas.findRegion("wall"), 16 * 2, 16 * 0, 16, 16), 16 / gameManager.PPM, 16 / gameManager.PPM);

            } else {
                Renderer = new renderer(new TextureRegion(textureAtlas.findRegion("wall"), 16 * 2, 16 * 1, 16, 16), 16 / gameManager.PPM, 16 / gameManager.PPM);
            }
        } else if (y < 1.0f) {
            Renderer = new renderer(new TextureRegion(textureAtlas.findRegion("wall"), 16 * 1, 16 * 2, 16, 16), 16 / gameManager.PPM, 16 / gameManager.PPM);

        } else if (y > mapH - 1) {
            Renderer = new renderer(new TextureRegion(textureAtlas.findRegion("wall"), 16 * 1, 16 * 0, 16, 16), 16 / gameManager.PPM, 16 / gameManager.PPM);

        } else {
            Renderer = new renderer(new TextureRegion(textureAtlas.findRegion("wall"), 0, 0, 16, 16), 16 / gameManager.PPM, 16 / gameManager.PPM);
        }

        Renderer.setSpriteOrigin(16 / gameManager.PPM / 2, 16 / gameManager.PPM / 2);
        new EntityBuilder(world).with(
                new transform(x, y, 1.0f, 1.0f, 0),
                Renderer
        ).build();
    }

    public void createIndestructable(float x, float y, TextureAtlas textureAtlas) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        Body body = box2DWorld.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = gameManager.INDESTRUCTABLE_BIT;
        fixtureDef.filter.maskBits = gameManager.PLAYER_BIT | gameManager.ENEMY_BIT | gameManager.BOMB_BIT;
        body.createFixture(fixtureDef);
        shape.dispose();

        renderer Renderer = new renderer(new TextureRegion(textureAtlas.findRegion("indestructible"), 0, 0, 16, 16), 16 / gameManager.PPM, 16 / gameManager.PPM);
        Renderer.setSpriteOrigin(16 / gameManager.PPM / 2, 16 / gameManager.PPM / 2);

        new EntityBuilder(world).with(
                new transform(x, y, 1.0f, 1.0f, 0),
                Renderer
        ).build();
    }

    public void createBreakableObj(float x, float y, TextureAtlas textureAtlas) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x, y);

        Body body = box2DWorld.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = gameManager.BREAKABLE_BIT;
        fixtureDef.filter.maskBits = gameManager.PLAYER_BIT | gameManager.ENEMY_BIT | gameManager.BOMB_BIT | gameManager.EXPLOSION_BIT;
        body.createFixture(fixtureDef);
        shape.dispose();

        HashMap<String, Animation> animas = new HashMap<>();
        TextureRegion textureRegion = textureAtlas.findRegion("breakable");

        Array<TextureRegion> keyFrames = new Array<>();
        for (int i = 0; i < 4; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 0, 16, 16));
        }
        Animation anima = new Animation(0.15f, keyFrames, Animation.PlayMode.LOOP);
        animas.put("normal", anima);

        keyFrames.clear();
        for (int i = 4; i < 10; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 0, 16, 16));
        }
        //Reassign anima.
        anima = new Animation(0.125f, keyFrames, Animation.PlayMode.NORMAL);
        animas.put("exploding", anima);

        renderer Renderer = new renderer(new TextureRegion(textureRegion, 0, 0, 16, 16), 16 / gameManager.PPM, 16 / gameManager.PPM);
        Renderer.setSpriteOrigin(16 / gameManager.PPM / 2, 16 / gameManager.PPM / 2);

        Entity e = new EntityBuilder(world).with(
                new breakableObj(),
                new transform(x, y, 1, 1, 0),
                new rigidBody(body),
                new state("normal"),
                Renderer,
                new anima(animas)
        ).build();

        body.setUserData(e);
    }
}
