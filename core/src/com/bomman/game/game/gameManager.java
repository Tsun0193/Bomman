package com.bomman.game.game;

import com.artemis.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import java.util.LinkedList;
import java.util.Queue;

public class gameManager implements Disposable {
    public static boolean playerKickBomb;
    public static boolean playerRemoteBomb;
    private final AssetManager assetManager;
    private static final gameManager instance = new gameManager();
    public static final float PPM = 16.0f;
    /* TODO: Game Properties. */
    public static final int levels = 5;
    public static int enemiesLeft;
    public static boolean gameOver;
    public static boolean gameFinished;
    private final Queue<Entity> remoteBombQueue;

    public static final short NOTHING_BIT = 0;
    public static final short INDESTRUCTABLE_BIT = 1;
    public static final short BREAKABLE_BIT = 1 << 1;
    public static final short PLAYER_BIT = 1 << 2;
    public static final short BOMB_BIT = 1 << 3;
    public static final short EXPLOSION_BIT = 1 << 4;
    public static final short ENEMY_BIT = 1 << 5;
    public static final short POWERUP_BIT = 1 << 6;
    public static final short PORTAL_BIT = 1 << 7;

    /* TODO: Player Properties. */
    public static int playerLives = 3;
    public static int playerBombCount = 3;
    public static int playerBombRemaining = 0;
    public static int playerMaxSpeed = 0;
    public static int playerBombPow = 1;
    public static float playerBombGenerateTime = 0.0f;
    public static float playerBombGenerateTimeLeft = 0.0f;
    public static boolean playerBombInteractablity = false;
    public static boolean infLives;
    public static boolean reset;

    private final Vector2 playerResPos;
    private final Vector2 playerGoalPos;

    /* Media Properties. */
    private String currentMusic = "";

    /**
     * Constructor.
     */
    private gameManager() {
        this.assetManager = new AssetManager();
        assetManager.load("img/actors.pack", TextureAtlas.class);

        remoteBombQueue = new LinkedList<>();

        /* TODO: Load sounds. */
        loadSound(assetManager);

        /* TODO: Load BGM. */
        loadMusic(assetManager);

        /* TODO: Load maps. */
        loadMaps(assetManager);

        assetManager.finishLoading();
        playerResPos = new Vector2();
        playerGoalPos = new Vector2();
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
    public static void resetPlayerAttributes() {
        playerMaxSpeed = 3;
        playerBombCount = 3;
        playerBombPow = 2;
        playerBombInteractablity = false;
        playerBombGenerateTime = 2.0f;
        playerRemoteBomb = false;
    }


    /**
     * Add Live by 1.
     */
    public void addLive()   {
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


    public void playMusic(String name, boolean repeat) {
        Music music = assetManager.get("music/" + name);
        music.setVolume(0.4f);
        /* Repeating Music. */
        if (currentMusic.equals(name)) {
            music.setLooping(repeat);
            if (!music.isPlaying()) {
                music.play();
            }
            return;
        }
        stopMusic();
        music.setLooping(repeat);
        music.play();
        currentMusic = name;
    }


    public void playMusic() {
        if (!currentMusic.isEmpty()) {
            Music music = assetManager.get("music/" + currentMusic, Music.class);
            music.play();
        }
    }


    public void pauseMusic() {
        if (!currentMusic.isEmpty()) {
            Music music = assetManager.get("music/" + currentMusic, Music.class);
            if (music.isPlaying()) {
                music.pause();
            }
        }
    }


    public void stopMusic() {
        if (!currentMusic.isEmpty()) {
            Music music = assetManager.get("music/" + currentMusic, Music.class);
            if (music.isPlaying()) {
                music.stop();
            }
        }
    }

    //Getters and Setters

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


    /**
     * Player Respawn Position Setter.
     *
     * @param pos Vector2
     */
    public void setPlayerResPos(Vector2 pos) {
        playerResPos.set(pos);
    }


    /**
     * Player Respawn Position Getter.
     *
     * @return playerResPos
     */
    public Vector2 getPlayerResPos() {
        return playerResPos;
    }


    /**
     * Player Goal Position Setter.
     *
     * @param playerGoalPos Vector2.
     */
    public void setPlayerGoalPos(Vector2 playerGoalPos) {
        this.playerGoalPos.set(playerGoalPos);
    }


    /**
     * Player Goal Position Getter.
     *
     * @return playerGoalPos.
     */
    public Vector2 getPlayerGoalPos() {
        return playerGoalPos;
    }


    /**
     * Remote Bomb Queue Getter.
     *
     * @return remoteBombQueue
     */
    public Queue<Entity> getRemoteBombQueue() {
        return remoteBombQueue;
    }


    /**
     * EnemiesLeft Setter.
     *
     * @param value int
     */
    public static void setEnemiesLeft(int value) {
        enemiesLeft = value;
    }


    /**
     * GameFinished Setter.
     *
     * @param bool boolean
     */
    public static void setGameFinished(boolean bool) {
        gameFinished = bool;
    }


    /**
     * GameOver Setter.
     *
     * @param bool boolean
     */
    public static void setGameOver(boolean bool) {
        gameFinished = bool;
    }

    public static void difficultyRespawn(boolean b1, boolean b2) {
        infLives = b1;
        reset = b2;
    }
    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
/* Final */
