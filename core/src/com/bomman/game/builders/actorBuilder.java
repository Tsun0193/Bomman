package com.bomman.game.builders;

import com.artemis.Entity;
import com.artemis.utils.EntityBuilder;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.bomman.game.components.*;
import com.bomman.game.game.gameManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
                Renderer = new renderer(new TextureRegion(textureAtlas.findRegion("wall"), 0, 0, 16, 16), 16 / gameManager.PPM, 16 / gameManager.PPM);

            } else {
                Renderer = new renderer(new TextureRegion(textureAtlas.findRegion("wall"), 0, 16, 16, 16), 16 / gameManager.PPM, 16 / gameManager.PPM);

            }
        } else if (x > mapW - 1) {
            if (y < 1.0f) {
                Renderer = new renderer(new TextureRegion(textureAtlas.findRegion("wall"), 16 * 2, 16 * 2, 16, 16), 16 / gameManager.PPM, 16 / gameManager.PPM);

            } else if (y > mapH - 1) {
                Renderer = new renderer(new TextureRegion(textureAtlas.findRegion("wall"), 16 * 2, 0, 16, 16), 16 / gameManager.PPM, 16 / gameManager.PPM);

            } else {
                Renderer = new renderer(new TextureRegion(textureAtlas.findRegion("wall"), 16 * 2, 16, 16, 16), 16 / gameManager.PPM, 16 / gameManager.PPM);
            }
        } else if (y < 1.0f) {
            Renderer = new renderer(new TextureRegion(textureAtlas.findRegion("wall"), 16, 16 * 2, 16, 16), 16 / gameManager.PPM, 16 / gameManager.PPM);

        } else if (y > mapH - 1) {
            Renderer = new renderer(new TextureRegion(textureAtlas.findRegion("wall"), 16, 0, 16, 16), 16 / gameManager.PPM, 16 / gameManager.PPM);

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

    public void createPlayer(float x, float y, boolean resetPlayerAbilities) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.linearDamping = 12.0f;

        Body body = box2DWorld.createBody(bodyDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = gameManager.PLAYER_BIT;
        fixtureDef.filter.maskBits = character.defaultMaskBits;
        body.createFixture(fixtureDef);
        shape.dispose();

        HashMap<String, Animation> animas = new HashMap<>();
        TextureAtlas textureAtlas = assetManager.get("img/actors.pack", TextureAtlas.class);
        TextureRegion textureRegion = textureAtlas.findRegion("Bomberman1");
        Animation anima = null;
        Array<TextureRegion> keyFrames = new Array<>();
        List<String> stringList = Arrays.asList("walking_up", "walking_left", "walking_down", "walking_right",
                "idle_up", "idle_left", "idle_down", "idle_right", "dying", "teleporting");
        for(int i=0;i< stringList.size();i++) {
            createPlayerMovement(keyFrames, anima, textureRegion, animas, stringList, i);
        }

        renderer Renderer = new renderer(new TextureRegion(textureRegion, 0, 0, 16, 24), 16 / gameManager.PPM, 24 / gameManager.PPM);
        Renderer.setSpriteOrigin(16 / gameManager.PPM / 2, 16 / gameManager.PPM / 2);
        Entity e = new EntityBuilder(world).with(
                new character(resetPlayerAbilities),
                new transform(x,y,1,1,0),
                new rigidBody(body),
                new state("idling_down"),
                Renderer,
                new anima(animas)
        ).build();
        body.setUserData(e);
    }

    public void createPlayerMovement(Array<TextureRegion> keyFrames, Animation anima, TextureRegion textureRegion, HashMap<String, Animation> animas,
                                     List<String> stringList, int id) {
        keyFrames.clear();
        if (id <= 3 || id == 8) {   //walking + dying
            for (int i = id; i < id + 3; i++) {
                keyFrames.add(new TextureRegion(textureRegion, i * 16, 0, 16, 24));
            }
            if (id == 8) {
                anima = new Animation(0.1f, keyFrames, Animation.PlayMode.NORMAL);
            } else anima = new Animation(0.1f, keyFrames, Animation.PlayMode.LOOP);
        } else if (id == 9) {       //teleporting
            keyFrames.add(new TextureRegion(textureRegion, 16, 0, 16, 24));
            keyFrames.add(new TextureRegion(textureRegion, 16 * 3, 0, 16, 24));
            keyFrames.add(new TextureRegion(textureRegion, 16 * 7, 0, 16, 24));
            keyFrames.add(new TextureRegion(textureRegion, 16 * 9, 0, 16, 24));
            anima = new Animation(0.05f, keyFrames, Animation.PlayMode.LOOP);
        } else {                    //idle
            if (id == 4 || id == 5) {
                keyFrames.add(new TextureRegion(textureRegion, (2 * id - 7) * 16, 0, 16, 24));
            } else {
                keyFrames.add(new TextureRegion(textureRegion, (2 * id - 5) * 16, 0, 16, 24));
            }
            anima = new Animation(0.1f, keyFrames, Animation.PlayMode.NORMAL);
        }
        animas.put(stringList.get(id), anima);
    }

    public void createBomb(character Character, float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(MathUtils.floor(x) + 0.5f, MathUtils.floor(y) + 0.5f);

        Body body = box2DWorld.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.45f, 0.45f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = gameManager.BOMB_BIT;
        fixtureDef.filter.maskBits = bomb.defaultMaskBits;
        body.createFixture(fixtureDef);
        shape.dispose();

        TextureAtlas textureAtlas = assetManager.get("img/actors.pack", TextureAtlas.class);
        HashMap<String, Animation> animas = new HashMap<>();
        TextureRegion textureRegion = textureAtlas.findRegion("Bomb");

        Animation anima;
        Array<TextureRegion> keyFrames = new Array<>();
        if (Character.bombPower >= character.MAX_BOMB_POWER) {
            for (int i = 0; i < 3; i++) {
                keyFrames.add(new TextureRegion(textureRegion, i * 16, 16, 16, 16));
            }
        } else {
            for (int i = 0; i < 3; i++) {
                keyFrames.add(new TextureRegion(textureRegion, i * 16, 0, 16, 16));
            }
        }
        anima = new Animation(0.15f, keyFrames, Animation.PlayMode.LOOP_PINGPONG);
        animas.put("normal", anima);

        renderer Renderer = new renderer(new TextureRegion(textureRegion, 0, 0, 16, 16), 16 / gameManager.PPM, 16 / gameManager.PPM);
        Renderer.setSpriteOrigin(16 / gameManager.PPM / 2, 16 / gameManager.PPM / 2);

        Entity e = new EntityBuilder(world).with(
                        new bomb(Character.bombPower, 2.0f),
                        new transform(body.getPosition().x, body.getPosition().y, 1, 1, 0),
                        new rigidBody(body),
                        new state("normal"),
                        Renderer,
                        new anima(animas)
                ).build();

        body.setUserData(e);
    }
}
