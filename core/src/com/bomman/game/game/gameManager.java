package com.bomman.game.game;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.assets.AssetManager;

public class gameManager implements Disposable {
    private final AssetManager assetManager;
    private static final gameManager instance = new gameManager();

    /* TODO: Game Properties. */
    private static final int levels = 5;


    /* TODO: Player Properties. */
    private static int playerLives = 3;
    private static int playerBombCount = 3;
    private static int playerBombRemaining = 0;
    private static int playerMaxSpeed = 0;
    private static int playerBombPow = 0;
    private static float playerBombGenerateTime = 0.0f;
    private static float playerBombGenerateTimeLeft = 0.0f;
    private static boolean playerBombInteractablity = false;


    /**
     * Constructor.
     */
    private gameManager() {
        this.assetManager = new AssetManager();
        assetManager.load("img/actors.pack", TextureAtlas.class);

        /* TODO: Load sounds. */
        loadSound(assetManager);

        /* TODO: Load BGM. */
        loadMusic(assetManager);

        /* TODO: Load maps. */
        loadMaps(assetManager);

        assetManager.finishLoading();
    }


    /**
     * Load Sounds for Asset Manager.
     *
     * @param am Asset Manager
     */
    public void loadSound(AssetManager am) {
        am.load("sounds/Pickup.ogg", Sound.class);
        am.load("sounds/PlaceBomb.ogg", Sound.class);
        am.load("sounds/KickBomb.ogg", Sound.class);
        am.load("sounds/Powerup.ogg", Sound.class);
        am.load("sounds/Explosion.ogg", Sound.class);
        am.load("sounds/Die.ogg", Sound.class);
        am.load("sounds/EnemyDie.ogg", Sound.class);
        am.load("sounds/EnemyDie1.ogg", Sound.class);
        am.load("sounds/EnemyDie2.ogg", Sound.class);
        am.load("sounds/Boss1Hammer.ogg", Sound.class);
        am.load("sounds/PortalAppears.ogg", Sound.class);
        am.load("sounds/Teleport.ogg", Sound.class);
        am.load("sounds/Pause.ogg", Sound.class);
    }


    /**
     * Load Musics for Asset Manager.
     *
     * @param am Asset Manager
     */
    public void loadMusic(AssetManager am) {
        am.load("music/SuperBomberman-Title.ogg", Music.class);
        am.load("music/SuperBomberman-Area1.ogg", Music.class);
        am.load("music/SuperBomberman-Area2.ogg", Music.class);
        am.load("music/SuperBomberman-Boss.ogg", Music.class);
        am.load("music/GameOver.ogg", Music.class);
        am.load("music/Victory.ogg", Music.class);
        am.load("music/Oops.ogg", Music.class);
        am.load("music/StageCleared.ogg", Music.class);
    }


    /**
     * Load Maps for Asset Manager.
     *
     * @param am Asset Manager
     */
    public void loadMaps(AssetManager am) {
        am.load("maps/level_1.png", Pixmap.class);
        am.load("maps/level_2.png", Pixmap.class);
        am.load("maps/level_3.png", Pixmap.class);
        am.load("maps/level_4.png", Pixmap.class);
        am.load("maps/level_5.png", Pixmap.class);
        am.load("maps/area_1_tiles.pack", TextureAtlas.class);
        am.load("maps/area_2_tiles.pack", TextureAtlas.class);
        am.load("maps/area_3_tiles.pack", TextureAtlas.class);
    }


    /**
     * Reset Player Properties Function.
     */
    public void resetPlayerProperties() {
        playerMaxSpeed = 0;
        playerBombCount = 3;
        playerBombPow = 0;
        playerBombInteractablity = false;
        playerBombGenerateTime = 3.0f;
    }


    /**
     * Add Live by 1.
     */
    public void addLive(){
        playerLives++;
        playSound("Powerup.ogg");
    }


    /**
     * Play Sound Function via Sound name.
     *
     * @param name String
     */
    public void playSound(String name) {
        playSound(name, 1.0f, 1.0f, 1.0f);
    }


    /**
     * Play Sound Function Details.
     *
     * @param name  String
     * @param vol   float
     * @param pitch float
     * @param pan   float
     */
    public void playSound(String name, float vol, float pitch, float pan) {
        Sound sound = assetManager.get("sounds/" + name, Sound.class);
        sound.play(vol, pitch, pan);
    }


    /**
     * Asset Manager getter.
     *
     * @return Asset Manager
     */
    public AssetManager getAssetManager() {
        return assetManager;
    }

    /**
     * Instance getter.
     *
     * @return Instance
     */
    public static gameManager getInstance() {
        return instance;
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
