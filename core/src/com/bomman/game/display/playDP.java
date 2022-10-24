package com.bomman.game.display;

import com.artemis.BaseSystem;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bomman.game.BGame;
import com.bomman.game.builders.worldBuilder;
import com.bomman.game.checkpoint.checkpoint;
import com.bomman.game.game.gameManager;
import com.bomman.game.gui.hud;
import com.bomman.game.listeners.box2dListener;
import com.bomman.game.sys.*;

public class playDP extends ScreenAdapter {
    private final BGame bGame;
    private final SpriteBatch batch;
    private FitViewport viewport;
    private Stage stage;
    private Stage stage2;

    private int mapWidth;
    private int mapHeight;
    private final int level;
    private float box2dTimer;
    private boolean pauseFlag;
    private boolean changeScr;

    private Window pauseWindow;
    private OrthographicCamera camera;
    private World box2DWorld;
    private com.artemis.World world;

    private Box2DDebugRenderer box2DRenderer;
    private boolean box2DRendererFlag;
    private Texture fadeout;
    private Sprite groundSprite;


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
    @Override
    public void show() {
        camera = new OrthographicCamera();
        float WIDTH = 20;
        float HEIGHT = 15;
        viewport = new FitViewport(WIDTH, HEIGHT, camera);
        camera.position.set(WIDTH / 2, HEIGHT / 2, 0);

        Box2D.init();
        box2DWorld = new World(new Vector2(), true);
        box2DWorld.setContactListener(new box2dListener());
        box2DRenderer = new Box2DDebugRenderer();

        WorldConfiguration worldCfg = new WorldConfigurationBuilder().with(
                new playerSys(),
                new bombSys(),
                new explosionSys(),
                new buffSys(),
                new enemySys(),
                new breakObjSys(),
                new physicsSys(),
                new stateSys(),
                new animaSys(),
                new renderSys(batch),
                new particleSys(batch)
        ).build();
        world = new com.artemis.World(worldCfg);

        gameManager.setEnemiesLeft(0);
        gameManager.setGameFinished(false);
        gameManager.setGameOver(false);

        worldBuilder builder = new worldBuilder(box2DWorld, world);
        builder.build(level);
        groundSprite = builder.getSprite();

        mapWidth = builder.getMapWidth();
        mapHeight = builder.getMapHeight();

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
        changeScr = false;
        stage = new Stage(viewport);
        Pixmap pixmap = new Pixmap((int) WIDTH, (int) HEIGHT, Pixmap.Format.RGB888);
        pixmap.setColor(0.2f, 0.2f, 0.2f, 1.0f);
        pixmap.fill();
        fadeout = new Texture(pixmap);
        pixmap.dispose();
        Image img = new Image(fadeout);
        stage.addActor(img);
        stage.addAction(Actions.fadeOut(0.5f));
        pauseFlag = false;

        stage2 = new Stage(new FitViewport(640, 480), batch);
        Skin skin = new Skin(Gdx.files.internal("uiskin/uiskin.json"));
        pauseWindow = new Window("Pause", skin);
        pauseWindow.setPosition((640 - pauseWindow.getWidth()) / 2, (480 - pauseWindow.getHeight()) / 2);
        pauseWindow.setVisible(pauseFlag);
        TextButton continueButton = new TextButton("Continue", skin);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                pauseFlag = false;
                gameManager.getInstance().playMusic();
            }
        });

        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                checkpoint Store = new checkpoint("Untitled Save");
                Store.setInt("playerLives", gameManager.playerLives);
                bGame.setScreen(new mainMenuDP(bGame));
            }
        });

        pauseWindow.add(continueButton).padBottom(16.0f);
        pauseWindow.row();
        pauseWindow.add(exitButton);
        stage2.addActor(pauseWindow);
        Gdx.input.setInputProcessor(stage2);
    }

    public void render(float f) {
        inputHandle();
        screenHandle();
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                groundSprite.setPosition(j, i);
                groundSprite.draw(batch);
            }
        }
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
                if (!(sys instanceof renderSys)) {
                    sys.setEnabled(false);
                }
            }
        }

        world.setDelta(f);
        world.process();

        Hud.draw(f);

        stage.draw();
        stage.act(f);

        pauseWindow.setVisible(pauseFlag);
        stage2.draw();

        if (box2DRendererFlag) {
            box2DRenderer.render(box2DWorld, camera.combined);
        }
    }

    private void screenHandle() {
        if (gameManager.gameFinished && !changeScr) {
            gameManager.getInstance().playSound("Teleport.ogg");
            stage.addAction(Actions.addAction(
                    Actions.sequence(
                            Actions.delay(1.0f),
                            Actions.fadeIn(1.0f),
                            Actions.delay(1.0f),
                            Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    if (level >= gameManager.levels) {
                                        bGame.setScreen(new endingDP(bGame));
                                    } else {
                                        bGame.setScreen(new playDP(bGame, level + 1));
                                    }
                                }
                            })
                    )));
            changeScr = true;
        }

        if (gameManager.gameOver && !changeScr) {
            stage.addAction(Actions.addAction(
                    Actions.sequence(
                            Actions.delay(1.0f),
                            Actions.fadeIn(1.0f),
                            Actions.delay(1.0f),
                            Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    bGame.setScreen(new finishDP(bGame));
                                }
                            })
                    )));
            changeScr = true;
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
        stage.dispose();
        fadeout.dispose();
        Hud.dispose();
    }
}
/* Final */
