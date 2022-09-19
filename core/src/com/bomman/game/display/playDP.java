package com.bomman.game.display;

import com.artemis.BaseSystem;
import com.artemis.WorldConfiguration;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bomman.game.BGame;
import com.bomman.game.game.gameManager;
import com.bomman.game.gui.hud;

public class playDP extends ScreenAdapter {
    private final BGame bGame;
    private final SpriteBatch batch;
    private FitViewport viewport;

    private int mapWidth;
    private int mapHeight;
    private int level;
    private float box2dTimer;
    private final float WIDTH = 20;
    private final float HEIGHT = 15;
    private boolean pauseFlag;


    private OrthographicCamera camera;
    private World box2DWorld;
    private com.artemis.World world;

    private Box2DDebugRenderer box2DRenderer;
    private boolean box2DRendererFlag;


    private hud Hud;

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
        gameManager.setGameFinished(false);
        gameManager.setGameOver(false);

        Hud = new hud(batch, WIDTH, HEIGHT);
        Hud.setLevelInfo(level);
        box2dTimer = 0;

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

    public void render(float f) {
        inputHandle();

        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.end();

        if (!pauseFlag) {
            box2dTimer += f;
            if (box2dTimer > 1 / 60.0f) {
                box2DWorld.step(1 / 60.0f, 8, 3);
                box2dTimer -= 1 / 60.0f;
            }

            for (BaseSystem sys : world.getSystems()) {
                sys.setEnabled(true);
            }
        } else {
            for (BaseSystem sys : world.getSystems()) {
//                if (!(sys instanceof RenderSystem)) {
                    sys.setEnabled(false);
//                }
            }
        }

        world.setDelta(f);
        world.process();

        Hud.draw(f);

        if (box2DRendererFlag) {
            box2DRenderer.render(box2DWorld, camera.combined);
        }
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
