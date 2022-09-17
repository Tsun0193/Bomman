package com.bomman.game.display;

import com.artemis.BaseSystem;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.physics.box2d.World;
import com.bomman.game.BGame;
import com.bomman.game.game.gameManager;

public class playDP extends ScreenAdapter {
    private final BGame bGame;
    private final SpriteBatch batch;
    private FitViewport viewport;

    private int mapWidth;
    private int mapHeight;
    private int level;
    private final float WIDTH = 20;
    private final float HEIGHT = 15;
    private boolean pauseFlag;


    private OrthographicCamera camera;
    private World box2DWorld;
    private com.artemis.World world;

    private Box2DDebugRenderer box2DRenderer;
    private boolean box2DRendererFlag;

    /**
     * Constructor.
     *
     * @param bGame bGame
     */
    public playDP(BGame bGame, int level) {
        this.bGame = bGame;
        this.batch = bGame.getBatch();
        this.level = level;
        box2DRendererFlag = false;
    }


    /**
     * Main Function for Playing Display.
     */
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(WIDTH, HEIGHT, camera);
        camera.position.set(WIDTH / 2, HEIGHT / 2, 0);

        box2DWorld = new World(new Vector2(), true);
        box2DRenderer = new Box2DDebugRenderer();

        WorldConfiguration worldCfg = new WorldConfiguration();
        world = new com.artemis.World(worldCfg);

        gameManager.setEnemiesLeft(0);
        gameManager.setGameFinished(true);
        gameManager.setGameOver(true);

        switch (level) {
            default:
                gameManager.getInstance().playMusic("SuperBomberman-Area1.ogg", true);
                break;
            case 1:
            case 2:
            case 3:
                gameManager.getInstance().playMusic("SuperBomberman-Area2.ogg", true);
                break;
            case 4:
            case 5:
                gameManager.getInstance().playMusic("SuperBomberman-Boss.ogg", true);
                break;
        }
        pauseFlag = false;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public void inputHandle() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            box2DRendererFlag = !box2DRendererFlag;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pauseFlag = !pauseFlag;
            if (pauseFlag) {
                gameManager.getInstance().playSound("Pause.ogg");
                gameManager.getInstance().pauseMusic();
            } else gameManager.getInstance().playMusic();
        }
    }

    @Override
    public void hide() {
        gameManager.getInstance().stopMusic();
        dispose();
    }


    @Override
    public void dispose() {
        box2DWorld.dispose();
        world.dispose();
        box2DRenderer.dispose();
    }
}
