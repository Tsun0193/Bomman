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

import java.util.HashMap;

public class actorBuilder {
    /**
     * Declare variables.
     */
    private static final actorBuilder instance = new actorBuilder();
    protected final float radius = 0.45f;
    /**
     * 2 World song song cung hoat dong, moi cai xu ly mot phan rieng.
     */
    private World box2DWorld; //Xu li tuong tac vat ly, a.k.a. Mechanics.
    private com.artemis.World world; //Xu li entities,  a.k.a Controllers.
    private AssetManager assetManager;
    private final Vector2 fromV = new Vector2();
    private final Vector2 toV = new Vector2();
    private boolean explodeThrough;

    /**
     * Default Constructor.
     */
    private actorBuilder() {
        //Do nothing.
    }

    /**
     * Initializing actors: Asset Manager, World.
     * @param box2DWorld mechanical world
     * @param world controlling systems
     * @return instance (actorBuilder)
     */
    public static actorBuilder init(World box2DWorld, com.artemis.World world) {
        instance.box2DWorld = box2DWorld;
        instance.assetManager = gameManager.getInstance().getAssetManager();
        instance.world = world;
        return instance;
    }

    /**
     * Below are creating components methods.
     * <p>
     * In common, bodyDef = component body definitions: set types and set coordinates.
     * shape = shaped frame for fixture.
     * fixtureDef = object created with aforementioned shape.
     * Last step is to render the above fixture into world (controller).
     * </p>
     * <p>
     * P/s:
     * categoryBits -> categorizing which type of bit that is.
     * maskBits -> selecting which type of bit can collide with.
     * </p>
     */

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

    /**
     * Always dispose unused variables to optimize application's execution.
     */
    public void createIndestructible(float x, float y, TextureAtlas textureAtlas) {
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

    /**
     * <p>
     *     New types of Creation - Unstable
     * </p>
     * <p>
     *     Hashmap of Animations to make objects moving, including states.
     * </p>
     * <p>
     *     TextureAtlas - Album, providing a way to manage textures.
     *     While keyframes actually just indexes of pics, mainly used for creating smooth movement.
     * </p>
     * <p></p>
     * Lastly, set user data (entity) into body.
     */

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
        Animation anima;

        Array<TextureRegion> keyFrames = new Array<>();
        // walking up
        for (int i = 0; i < 3; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 0, 16, 24));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.LOOP);
        animas.put("walking_up", anima);

        // walking left
        keyFrames.clear();
        for (int i = 3; i < 6; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 0, 16, 24));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.LOOP);
        animas.put("walking_left", anima);

        // walking down
        keyFrames.clear();
        for (int i = 6; i < 9; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 0, 16, 24));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.LOOP);
        animas.put("walking_down", anima);

        // walking right
        keyFrames.clear();
        for (int i = 9; i < 12; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 0, 16, 24));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.LOOP);
        animas.put("walking_right", anima);

        // idling up
        keyFrames.clear();
        keyFrames.add(new TextureRegion(textureRegion, 1 * 16, 0, 16, 24));
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.NORMAL);
        animas.put("idling_up", anima);

        // idling left
        keyFrames.clear();
        keyFrames.add(new TextureRegion(textureRegion, 3 * 16, 0, 16, 24));
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.NORMAL);
        animas.put("idling_left", anima);

        // idling down
        keyFrames.clear();
        keyFrames.add(new TextureRegion(textureRegion, 7 * 16, 0, 16, 24));
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.NORMAL);
        animas.put("idling_down", anima);

        // idling right
        keyFrames.clear();
        keyFrames.add(new TextureRegion(textureRegion, 9 * 16, 0, 16, 24));
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.NORMAL);
        animas.put("idling_right", anima);

        // dying
        keyFrames.clear();
        for (int i = 12; i < 18; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 0, 16, 24));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.NORMAL);
        animas.put("dying", anima);

        // teleporting
        keyFrames.clear();
        keyFrames.add(new TextureRegion(textureRegion, 16 * 1, 0, 16, 24));
        keyFrames.add(new TextureRegion(textureRegion, 16 * 3, 0, 16, 24));
        keyFrames.add(new TextureRegion(textureRegion, 16 * 7, 0, 16, 24));
        keyFrames.add(new TextureRegion(textureRegion, 16 * 9, 0, 16, 24));
        anima = new Animation(0.05f, keyFrames, Animation.PlayMode.LOOP);
        animas.put("teleporting", anima);

        renderer Renderer = new renderer(new TextureRegion(textureRegion, 0, 0, 16, 24), 16 / gameManager.PPM, 24 / gameManager.PPM);
        Renderer.setSpriteOrigin(16 / gameManager.PPM / 2, 16 / gameManager.PPM / 2);

        // entity
        Entity e = new com.artemis.utils.EntityBuilder(world)
                .with(
                        new character(resetPlayerAbilities),
                        new transform(x, y, 1, 1, 0),
                        new rigidBody(body),
                        new state("idling_down"),
                        Renderer,
                        new anima(animas)
                )
                .build();

        body.setUserData(e);
    }

    public void createOctopus(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearDamping = 12.0f;
        bodyDef.position.set(x, y);

        Body body = box2DWorld.createBody(bodyDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = gameManager.ENEMY_BIT;
        fixtureDef.filter.maskBits = Enemy.defaultMaskBits;
        body.createFixture(fixtureDef);

        shape.dispose();

        HashMap<String, Animation> animas = new HashMap<>();
        TextureAtlas textureAtlas = assetManager.get("img/actors.pack", TextureAtlas.class);
        TextureRegion textureRegion = textureAtlas.findRegion("Octopus");
        Animation anima;

        Array<TextureRegion> keyFrames = new Array<>();
        // walking down
        for (int i = 0; i < 4; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 0, 16, 24));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.LOOP);
        animas.put("walking_down", anima);

        keyFrames.clear();
        // walking up
        for (int i = 4; i < 8; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 0, 16, 24));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.LOOP);
        animas.put("walking_up", anima);

        keyFrames.clear();
        // walking left
        for (int i = 8; i < 12; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 0, 16, 24));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.LOOP);
        animas.put("walking_left", anima);

        keyFrames.clear();
        // walking right
        for (int i = 8; i < 12; i++) {
            TextureRegion textureRegionRight = new TextureRegion(textureRegion, i * 16, 0, 16, 24);
            textureRegionRight.flip(true, false);
            keyFrames.add(textureRegionRight);
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.LOOP);
        animas.put("walking_right", anima);

        keyFrames.clear();
        // dying
        for (int i = 12; i < 16; i++) {
            TextureRegion textureRegionRight = new TextureRegion(textureRegion, i * 16, 0, 16, 24);
            keyFrames.add(textureRegionRight);
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.NORMAL);
        animas.put("dying", anima);

        renderer Renderer = new renderer(new TextureRegion(textureRegion, 0, 0, 16, 24), 16 / gameManager.PPM, 24 / gameManager.PPM);
        Renderer.setSpriteOrigin(16 / gameManager.PPM / 2, 16 / gameManager.PPM / 2);

        Entity e = new EntityBuilder(world).with(
                new Enemy(1, 0.8f),
                new transform(x, y, 1, 1, 0),
                new rigidBody(body),
                new state("walking_down"),
                Renderer,
                new anima(animas)
        ).build();
        body.setUserData(e);
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

    public void createPortal() {
        float x = gameManager.getInstance().getPlayerGoalPos().x;
        float y = gameManager.getInstance().getPlayerGoalPos().y;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x + 0.5f, y + 0.5f);

        Body body = box2DWorld.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.2f, 0.2f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = gameManager.PORTAL_BIT;
        fixtureDef.filter.maskBits = gameManager.PLAYER_BIT;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef);

        shape.dispose();

        TextureRegion textureRegion = assetManager.get("img/actors.pack", TextureAtlas.class).findRegion("Items");
        Array<TextureRegion> keyFrames = new Array<>();
        for (int i = 6; i < 8; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 0, 16, 16));
        }
        Animation anima = new Animation(0.2f, keyFrames, Animation.PlayMode.LOOP);

        HashMap<String, Animation> animas = new HashMap<>();
        animas.put("normal", anima);

        transform Transform = new transform(body.getPosition().x, body.getPosition().y, 1, 1, 0);
        Transform.temp = 99;
        renderer Renderer = new renderer(textureRegion, 16 / gameManager.PPM, 16 / gameManager.PPM);
        Renderer.setSpriteOrigin(16 / gameManager.PPM / 2, 16 / gameManager.PPM / 2);

        Entity e = new EntityBuilder(world).with(
                Transform,
                new state("normal"),
                new anima(animas),
                Renderer
        ).build();

        body.setUserData(e);
    }

    public Entity createRemoteBomb(character Character, float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(MathUtils.floor(x) + 0.5f, MathUtils.floor(y) + 0.5f);

        Body body = box2DWorld.createBody(bodyDef);
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(0.45f, 0.45f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.filter.categoryBits = gameManager.BOMB_BIT;
        fixtureDef.filter.maskBits = bomb.defaultMaskBits;
        body.createFixture(fixtureDef);
        polygonShape.dispose();

        TextureAtlas textureAtlas = assetManager.get("img/actors.pack", TextureAtlas.class);
        HashMap<String, Animation> animas = new HashMap<>();
        TextureRegion textureRegion = textureAtlas.findRegion("Bomb");

        Animation anima;
        Array<TextureRegion> keyFrames = new Array<>();
        if (Character.bombPower >= character.MAX_BOMB_POWER) {
            for (int i = 3; i < 5; i++) {
                keyFrames.add(new TextureRegion(textureRegion, i * 16, 16 * 1, 16, 16));
            }
        } else {
            for (int i = 3; i < 5; i++) {
                keyFrames.add(new TextureRegion(textureRegion, i * 16, 0, 16, 16));
            }
        }
        anima = new Animation(0.15f, keyFrames, Animation.PlayMode.LOOP_PINGPONG);
        animas.put("normal", anima);

        renderer Renderer = new renderer(new TextureRegion(textureRegion, 0, 0, 16, 16), 16 / gameManager.PPM, 16 / gameManager.PPM);
        Renderer.setSpriteOrigin(16 / gameManager.PPM / 2, 16 / gameManager.PPM / 2);

        // entity
        Entity e = new EntityBuilder(world)
                .with(
                        new bomb(Character.bombPower, 16.0f),
                        new transform(body.getPosition().x, body.getPosition().y, 1, 1, 0),
                        new rigidBody(body),
                        new state("normal"),
                        Renderer,
                        new anima(animas)
                )
                .build();

        body.setUserData(e);
        return e;
    }

    public void createBombEnemy(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearDamping = 12.0f;
        bodyDef.position.set(x, y);

        Body body = box2DWorld.createBody(bodyDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = gameManager.ENEMY_BIT;
        fixtureDef.filter.maskBits = Enemy.defaultMaskBits;
        body.createFixture(fixtureDef);

        shape.dispose();

        HashMap<String, Animation> animas = new HashMap<>();
        TextureAtlas textureAtlas = assetManager.get("img/actors.pack", TextureAtlas.class);
        TextureRegion textureRegion = textureAtlas.findRegion("BombEnemy");
        Animation anima;

        Array<TextureRegion> keyFrames = new Array<>();
        // walking down
        for (int i = 0; i < 5; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 0, 16, 24));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.LOOP_PINGPONG);
        animas.put("walking_down", anima);

        keyFrames.clear();
        // walking up
        for (int i = 0; i < 5; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 24, 16, 24));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.LOOP_PINGPONG);
        animas.put("walking_up", anima);

        keyFrames.clear();
        // walking left
        for (int i = 0; i < 5; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 24 * 2, 16, 24));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.LOOP_PINGPONG);
        animas.put("walking_left", anima);

        keyFrames.clear();
        // walking right
        for (int i = 0; i < 5; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 24 * 3, 16, 24));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.LOOP_PINGPONG);
        animas.put("walking_right", anima);

        keyFrames.clear();
        // dying
        for (int i = 0; i < 1; i++) {
            // no dying sprite
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 0, 0, 0));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.NORMAL);
        animas.put("dying", anima);

        keyFrames.clear();
        // attacking (up)
        for (int i = 0; i < 5; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 24 * 4, 16, 24));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.LOOP_PINGPONG);
        animas.put("attacking_up", anima);

        renderer Renderer = new renderer(new TextureRegion(textureRegion, 0, 0, 16, 24), 16 / gameManager.PPM, 24 / gameManager.PPM);
        Renderer.setSpriteOrigin(16 / gameManager.PPM / 2, 16 / gameManager.PPM / 2);

        Entity e = new EntityBuilder(world).with(
                new Enemy(1, 1.0f, "EnemyDie2.ogg", "bomb"),
                new transform(x, y, 1, 1, 0),
                new rigidBody(body),
                new state("walking_down"),
                Renderer,
                new anima(animas)
        ).build();
        body.setUserData(e);
    }

    public void createSlime(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearDamping = 12.0f;
        bodyDef.position.set(x, y);

        Body body = box2DWorld.createBody(bodyDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = gameManager.ENEMY_BIT;
        fixtureDef.filter.maskBits = Enemy.defaultMaskBits;
        body.createFixture(fixtureDef);

        shape.dispose();

        // animation
        HashMap<String, Animation> animas = new HashMap<>();
        TextureAtlas textureAtlas = assetManager.get("img/actors.pack", TextureAtlas.class);
        TextureRegion textureRegion = textureAtlas.findRegion("Slime");
        Animation anima;

        Array<TextureRegion> keyFrames = new Array<>();
        // walking down
        for (int i = 0; i < 6; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 0, 16, 24));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.LOOP_PINGPONG);
        animas.put("walking_down", anima);

        keyFrames.clear();
        // walking up
        for (int i = 0; i < 6; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 0, 16, 24));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.LOOP_PINGPONG);
        animas.put("walking_up", anima);

        keyFrames.clear();
        // walking left
        for (int i = 0; i < 6; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 0, 16, 24));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.LOOP_PINGPONG);
        animas.put("walking_left", anima);

        keyFrames.clear();
        // walking right
        for (int i = 0; i < 6; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 0, 16, 24));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.LOOP_PINGPONG);
        animas.put("walking_right", anima);

        keyFrames.clear();
        // dying
        for (int i = 6; i < 9; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 0, 16, 24));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.NORMAL);
        animas.put("dying", anima);

        renderer Renderer = new renderer(new TextureRegion(textureRegion, 0, 0, 16, 24), 16 / gameManager.PPM, 24 / gameManager.PPM);
        Renderer.setSpriteOrigin(16 / gameManager.PPM / 2, 16 / gameManager.PPM / 2);

        Entity e = new EntityBuilder(world).with(
                new Enemy(1, 1.2f, "EnemyDie1.ogg"),
                new transform(x, y, 1, 1, 0),
                new rigidBody(body),
                new state("walking_down"),
                Renderer,
                new anima(animas)
        ).build();
        body.setUserData(e);
    }

    public void createHare(float x, float y) {
        // box2d
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearDamping = 12.0f;
        bodyDef.position.set(x, y);

        Body body = box2DWorld.createBody(bodyDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = gameManager.ENEMY_BIT;
        fixtureDef.filter.maskBits = Enemy.defaultMaskBits;
        body.createFixture(fixtureDef);

        shape.dispose();

        HashMap<String, Animation> animas = new HashMap<>();
        TextureAtlas textureAtlas = assetManager.get("img/actors.pack", TextureAtlas.class);
        TextureRegion textureRegion = textureAtlas.findRegion("Hare");
        Animation anima;

        Array<TextureRegion> keyFrames = new Array<>();
        // walking down
        for (int i = 0; i < 9; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 0, 16, 24));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.LOOP);
        animas.put("walking_down", anima);

        keyFrames.clear();
        // walking up
        for (int i = 0; i < 9; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 24, 16, 24));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.LOOP);
        animas.put("walking_up", anima);

        keyFrames.clear();
        // walking left
        for (int i = 0; i < 7; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 24 * 2, 16, 24));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.LOOP);
        animas.put("walking_left", anima);

        keyFrames.clear();
        // walking right
        for (int i = 0; i < 7; i++) {
            TextureRegion walkingRight = new TextureRegion(textureRegion, i * 16, 24 * 2, 16, 24);
            walkingRight.flip(true, false);
            keyFrames.add(walkingRight);
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.LOOP);
        animas.put("walking_right", anima);

        keyFrames.clear();
        // dying
        for (int i = 7; i < 9; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 24 * 2, 16, 24));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.NORMAL);
        animas.put("dying", anima);

        renderer Renderer = new renderer(new TextureRegion(textureRegion, 0, 0, 16, 24), 16 / gameManager.PPM, 24 / gameManager.PPM);
        Renderer.setSpriteOrigin(16 / gameManager.PPM / 2, 16 / gameManager.PPM / 2);

        Entity e = new EntityBuilder(world).with(
                        new Enemy(1, 1.6f, "EnemyDie2.ogg"),
                        new transform(x, y, 1, 1, 0),
                        new rigidBody(body),
                        new state("walking_down"),
                        Renderer,
                        new anima(animas)
                )
                .build();
        body.setUserData(e);
    }

    public void createBoss1(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.linearDamping = 12.0f;

        Body body = box2DWorld.createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(1.2f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.filter.categoryBits = gameManager.ENEMY_BIT;
        fixtureDef.filter.maskBits = gameManager.PLAYER_BIT | gameManager.EXPLOSION_BIT;
        body.createFixture(fixtureDef);
        circleShape.dispose();

        HashMap<String, Animation> animas = new HashMap<>();
        TextureAtlas textureAtlas = assetManager.get("img/actors.pack", TextureAtlas.class);
        TextureRegion textureRegion = textureAtlas.findRegion("Boss1");
        Animation anima;

        Array<TextureRegion> keyFrames = new Array<>();
        // walking up
        for (int i = 0; i < 1; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 80, 0, 80, 160));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.NORMAL);
        animas.put("walking_up", anima);

        keyFrames.clear();
        // walking up
        for (int i = 0; i < 1; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 80, 0, 80, 160));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.NORMAL);
        animas.put("walking_down", anima);

        keyFrames.clear();
        // walking left
        for (int i = 0; i < 1; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 80, 0, 80, 160));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.NORMAL);
        animas.put("walking_left", anima);

        keyFrames.clear();
        // walking right
        for (int i = 0; i < 1; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 80, 0, 80, 160));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.NORMAL);
        animas.put("walking_right", anima);

        keyFrames.clear();
        // dying
        for (int i = 0; i < 1; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 80, 0, 80, 160));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.NORMAL);
        animas.put("dying", anima);

        keyFrames.clear();
        // damaged
        for (int i = 4; i < 5; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 80, 0, 80, 160));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.NORMAL);
        animas.put("damaged", anima);

        keyFrames.clear();
        // attacking down
        for (int i = 1; i < 4; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 80, 0, 80, 160));
        }
        anima = new Animation(0.2f, keyFrames, Animation.PlayMode.NORMAL);
        animas.put("attacking_down", anima);

        renderer Renderer = new renderer(new TextureRegion(textureRegion, 0, 0, 80, 160), 80 / gameManager.PPM, 160 / gameManager.PPM);
        Renderer.setSpriteOrigin(80 / gameManager.PPM / 2, 160 / gameManager.PPM / 2);

        Entity e = new EntityBuilder(world).with(
                new Enemy(8, 1.2f, "EnemyDie1.ogg", "boss1"),
                new transform(x, y, 1, 1, 0),
                new rigidBody(body),
                new state("walking_down"),
                Renderer,
                new anima(animas)
        ).build();

        body.setUserData(e);

    }

    public void createBoss1Explosion(float x, float y) {
        new EntityBuilder(world).with(
                new particle("particles/boss1explode.particle", x, y)
        ).build();
    }

    /**
     * <p>
     *     The RayCastCallback interface is used to write code that gets executed when your ray hits a Fixture.
     * </p>
     * <p>
     *     Ray - invisible & imaginary line to check what it intersects with.
     * </p>
     * <p>
     *     e.g. : The Ray hit the fixture, while it can explode through -> continue impacting others on its way.
     * </p>
     */
    private boolean checkExplodeThrough(Vector2 fromV, Vector2 toV) {
        explodeThrough = true;
        RayCastCallback rayCastCallback = new RayCastCallback() {

            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if (fixture.getFilterData().categoryBits == gameManager.INDESTRUCTABLE_BIT) {
                    explodeThrough = false;
                    return 0;
                }

                if (fixture.getFilterData().categoryBits == gameManager.BREAKABLE_BIT) {
                    explodeThrough = false;
                    Entity e = (Entity) fixture.getBody().getUserData();
                    breakableObj obj = e.getComponent(breakableObj.class);
                    obj.state = breakableObj.State.explode;
                    return 0;
                }
                return 0;
            }
        };
        box2DWorld.rayCast(rayCastCallback, fromV, toV);
        return explodeThrough;
    }

    /**
     * Creating animations for explosions, and make contact (set objects destructed) with other objects.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param power affect range.
     */
    public void createExplosion(float x, float y, int power) {
        x = MathUtils.floor(x) + 0.5f;
        y = MathUtils.floor(y) + 0.5f;

        TextureRegion textureRegion = assetManager.get("img/actors.pack", TextureAtlas.class).findRegion("Explosion");
        HashMap<String, Animation> animas = new HashMap<>();

        Array<TextureRegion> keyFrames = new Array<>();
        Animation anima;

        // center
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        Body explosionBody = box2DWorld.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.3f, 0.3f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = gameManager.EXPLOSION_BIT;
        fixtureDef.filter.maskBits = explosion.defaultMaskBits;
        fixtureDef.isSensor = true;
        explosionBody.createFixture(fixtureDef);

        for (int i = 0; i < 5; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 16, 16, 16));
        }
        anima = new Animation(0.15f, keyFrames, Animation.PlayMode.NORMAL);
        animas.put("exploding", anima);

        renderer Renderer = new renderer(textureRegion, 1, 1);
        Renderer.setSpriteOrigin(16 / gameManager.PPM / 2, 16 / gameManager.PPM / 2);

        Entity e = new EntityBuilder(world).with(
                new explosion(),
                new transform(x, y, 1, 1, 0),
                new rigidBody(explosionBody),
                new state("exploding"),
                new anima(animas),
                Renderer
        ).build();
        explosionBody.setUserData(e);

        // up
        for (int i = 0; i < power; i++) {
            if (!checkExplodeThrough(fromV.set(x, y + i), toV.set(x, y + i + 1))) {
                break;
            }

            // box2d
            bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(x, y + i + 1);
            explosionBody = box2DWorld.createBody(bodyDef);
            fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = gameManager.EXPLOSION_BIT;
            fixtureDef.filter.maskBits = explosion.defaultMaskBits;
            fixtureDef.isSensor = true;
            explosionBody.createFixture(fixtureDef);

            keyFrames.clear();
            animas = new HashMap<>();

            for (int j = 0; j < 5; j++) {
                if (i + 1 == power) {
                    keyFrames.add(new TextureRegion(textureRegion, j * 16, 0, 16, 16));

                } else {
                    keyFrames.add(new TextureRegion(textureRegion, j * 16, 16 * 2, 16, 16));
                }
            }
            anima = new Animation(0.15f, keyFrames, Animation.PlayMode.NORMAL);
            animas.put("exploding", anima);

            Renderer = new renderer(textureRegion, 1, 1);
            Renderer.setSpriteOrigin(16 / gameManager.PPM / 2, 16 / gameManager.PPM / 2);

            new EntityBuilder(world).with(
                    new explosion(),
                    new transform(x, y + i + 1, 1, 1, 0),
                    new rigidBody(explosionBody),
                    new state("exploding"),
                    new anima(animas),
                    Renderer
            ).build();

            explosionBody.setUserData(e);
        }

        // down
        for (int i = 0; i < power; i++) {
            if (!checkExplodeThrough(fromV.set(x, y - i), toV.set(x, y - i - 1))) {
                break;
            }

            // box2d
            bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(x, y - i - 1);
            explosionBody = box2DWorld.createBody(bodyDef);
            fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = gameManager.EXPLOSION_BIT;
            fixtureDef.filter.maskBits = explosion.defaultMaskBits;
            fixtureDef.isSensor = true;
            explosionBody.createFixture(fixtureDef);

            keyFrames.clear();
            animas = new HashMap<>();

            for (int j = 0; j < 5; j++) {
                if (i + 1 == power) {
                    keyFrames.add(new TextureRegion(textureRegion, j * 16, 16 * 3, 16, 16));

                } else {
                    keyFrames.add(new TextureRegion(textureRegion, j * 16, 16 * 2, 16, 16));
                }
            }
            anima = new Animation(0.15f, keyFrames, Animation.PlayMode.NORMAL);
            animas.put("exploding", anima);

            Renderer = new renderer(textureRegion, 1, 1);
            Renderer.setSpriteOrigin(16 / gameManager.PPM / 2, 16 / gameManager.PPM / 2);

            new EntityBuilder(world).with(
                    new explosion(),
                    new transform(x, y - i - 1, 1, 1, 0),
                    new rigidBody(explosionBody),
                    new state("exploding"),
                    new anima(animas),
                    Renderer
            ).build();
            explosionBody.setUserData(e);
        }

        // left
        for (int i = 0; i < power; i++) {
            if (!checkExplodeThrough(fromV.set(x - i, y), toV.set(x - i - 1, y))) {
                break;
            }

            // box2d
            bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(x - i - 1, y);
            explosionBody = box2DWorld.createBody(bodyDef);
            fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = gameManager.EXPLOSION_BIT;
            fixtureDef.filter.maskBits = explosion.defaultMaskBits;
            fixtureDef.isSensor = true;
            explosionBody.createFixture(fixtureDef);

            keyFrames.clear();
            animas = new HashMap<>();

            for (int j = 0; j < 5; j++) {
                if (i + 1 == power) {
                    keyFrames.add(new TextureRegion(textureRegion, j * 16, 16 * 6, 16, 16));

                } else {
                    keyFrames.add(new TextureRegion(textureRegion, j * 16, 16 * 4, 16, 16));
                }
            }
            anima = new Animation(0.15f, keyFrames, Animation.PlayMode.NORMAL);
            animas.put("exploding", anima);

            Renderer = new renderer(textureRegion, 1, 1);
            Renderer.setSpriteOrigin(16 / gameManager.PPM / 2, 16 / gameManager.PPM / 2);

            new EntityBuilder(world).with(
                    new explosion(),
                    new transform(x - i - 1, y, 1, 1, 0),
                    new rigidBody(explosionBody),
                    new state("exploding"),
                    new anima(animas),
                    Renderer
            ).build();

            explosionBody.setUserData(e);
        }

        // right
        for (int i = 0; i < power; i++) {
            if (!checkExplodeThrough(fromV.set(x + i, y), toV.set(x + i + 1, y))) {
                break;
            }

            // box2d
            bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(x + i + 1, y);
            explosionBody = box2DWorld.createBody(bodyDef);
            fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = gameManager.EXPLOSION_BIT;
            fixtureDef.filter.maskBits = explosion.defaultMaskBits;
            fixtureDef.isSensor = true;
            explosionBody.createFixture(fixtureDef);

            keyFrames.clear();
            animas = new HashMap<>();

            for (int j = 0; j < 5; j++) {
                if (i + 1 == power) {
                    keyFrames.add(new TextureRegion(textureRegion, j * 16, 16 * 5, 16, 16));

                } else {
                    keyFrames.add(new TextureRegion(textureRegion, j * 16, 16 * 4, 16, 16));
                }
            }
            anima = new Animation(0.15f, keyFrames, Animation.PlayMode.NORMAL);
            animas.put("exploding", anima);

            Renderer = new renderer(textureRegion, 1, 1);
            Renderer.setSpriteOrigin(16 / gameManager.PPM / 2, 16 / gameManager.PPM / 2);

            new EntityBuilder(world).with(
                    new explosion(),
                    new transform(x + i + 1, y, 1, 1, 0),
                    new rigidBody(explosionBody),
                    new state("exploding"),
                    new anima(animas),
                    Renderer
            ).build();

            explosionBody.setUserData(e);
        }

        shape.dispose();
    }

    public void createBuff(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(MathUtils.floor(x) + 0.5f, MathUtils.floor(y) + 0.5f);

        Body body = box2DWorld.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(0.4f, 0.4f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.filter.categoryBits = gameManager.POWERUP_BIT;
        fixtureDef.filter.maskBits = gameManager.PLAYER_BIT;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef);

        buff Buff = new buff();
        int i;
        switch (Buff.type) {
            case ONE_UP:
                i = 5;
                break;
            case REMOTE:
                i = 4;
                break;
            case KICK:
                i = 3;
                break;
            case SPEED:
                i = 2;
                break;
            case POWER:
                i = 1;
                break;
            case AMMO:
            default:
                i = 0;
                break;

        }

        TextureAtlas textureAtlas = assetManager.get("img/actors.pack", TextureAtlas.class);
        renderer Renderer = new renderer(new TextureRegion(textureAtlas.findRegion("Items"), i * 16, 0, 16, 16), 16 / gameManager.PPM, 16 / gameManager.PPM);
        Renderer.setSpriteOrigin(16 / gameManager.PPM / 2, 16 / gameManager.PPM / 2);

        Entity e = new EntityBuilder(world).with(
                Buff,
                new rigidBody(body),
                new transform(body.getPosition().x, body.getPosition().y, 1, 1, 0),
                new state("normal"),
                Renderer
        ).build();

        body.setUserData(e);
        polygonShape.dispose();
    }
}
