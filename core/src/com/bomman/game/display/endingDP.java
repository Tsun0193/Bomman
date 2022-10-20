package com.bomman.game.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bomman.game.BGame;
import com.bomman.game.display.animaImg.animaImg;
import com.bomman.game.game.gameManager;

public class endingDP extends ScreenAdapter {
    private final float WIDTH = 640;
    private final float HEIGHT = 480;

    private final BGame game;
    private final SpriteBatch batch;
    private final AssetManager assetManager;

    private FitViewport viewport;
    private Stage stage;

    private BitmapFont font;

    private Label lastSentenceLabel;
    public endingDP(BGame bGame) {
        this.game = bGame;
        batch = game.getBatch();
        assetManager = gameManager.getInstance().getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(WIDTH, HEIGHT);
        stage = new Stage(viewport, batch);

        TextureRegion bomberman = assetManager.get("img/actors.pack", TextureAtlas.class).findRegion("Bomberman1");
        Array<TextureRegion> keyFrames = new Array<>();

        // Character walking animation
        for (int i = 9; i < 12; i++) {
            keyFrames.add(new TextureRegion(bomberman, 16 * i, 0, 16, 24));
        }
        Animation anima = new Animation(0.1f, keyFrames, Animation.PlayMode.LOOP);
        final animaImg bombermanAnimImage = new animaImg(new TextureRegion(bomberman, 0, 0, 16, 24));
        bombermanAnimImage.put("walking", anima);

        keyFrames.clear();
        // Character idling animation
        keyFrames.add(new TextureRegion(bomberman, 16 * 7, 0, 16, 24));
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.NORMAL);
        bombermanAnimImage.put("idling", anima);

        keyFrames.clear();
        // Character shocked animation
        for (int i = 12; i < 13; i++) {
            keyFrames.add(new TextureRegion(bomberman, 16 * i, 0, 16, 24));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.NORMAL);
        bombermanAnimImage.put("shocked", anima);

        bombermanAnimImage.setScale(2.5f);
        bombermanAnimImage.setPosition(180, 160);

        keyFrames.clear();

        // Princess walking animation
        TextureRegion princess = assetManager.get("img/actors.pack", TextureAtlas.class).findRegion("Princess");
        final animaImg princessAnimImage = new animaImg(new TextureRegion(princess, 0, 0, 16, 24));
        for (int i = 0; i < 3; i++) {
            keyFrames.add(new TextureRegion(princess, 16 * i, 0, 16, 24));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.LOOP);
        princessAnimImage.put("walking", anima);

        keyFrames.clear();

        // Princess idling animation
        for (int i = 3; i < 4; i++) {
            keyFrames.add(new TextureRegion(princess, 16 * i, 0, 16, 24));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.NORMAL);
        princessAnimImage.put("idling", anima);

        keyFrames.clear();

        // Princess dying animation
        for (int i = 5; i < 6; i++) {
            keyFrames.add(new TextureRegion(princess, 16 * i, 0, 16, 24));
        }
        anima = new Animation(0.1f, keyFrames, Animation.PlayMode.NORMAL);
        princessAnimImage.put("dying", anima);

        princessAnimImage.setCurrent("walking");
        princessAnimImage.setScale(2.5f);
        princessAnimImage.setPosition(480, 160);

        // actions
        bombermanAnimImage.addAction(
                Actions.sequence(
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                bombermanAnimImage.setCurrent("walking");
                            }
                        }),
                        Actions.moveBy(130, 0, 5f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                if (gameManager.playerLives > 0) {
                                    bombermanAnimImage.setCurrent("idling");
                                } else {
                                    bombermanAnimImage.setCurrent("shocked");
                                }
                            }
                        })
                )
        );

        princessAnimImage.addAction(
                Actions.sequence(
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                princessAnimImage.setCurrent("walking");
                            }
                        }),
                        Actions.moveBy(-140, 0, 5f),
                        Actions.run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        if (gameManager.playerLives > 0) {
                                            princessAnimImage.setCurrent("idling");
                                        } else {
                                            princessAnimImage.setCurrent("dying");
                                            gameManager.getInstance().playMusic("Oops.ogg", false);
                                            princessAnimImage.addAction(
                                                    Actions.sequence(
                                                            Actions.moveTo(360f, 300f, 1.0f),
                                                            Actions.moveTo(400f, -100f, 0.8f)
                                                    )
                                            );
                                        }
                                    }
                                })
                )
        );

        font = new BitmapFont(Gdx.files.internal("fonts/foo.fnt"));
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        Label congratulationsLabel = new Label("Congratulations!", labelStyle);
        congratulationsLabel.setPosition((WIDTH - congratulationsLabel.getWidth()) / 2, HEIGHT - congratulationsLabel.getHeight() - 52f);

        Label wordsLabel = new Label("You took the princess\n   from her father", labelStyle);
        wordsLabel.setPosition(WIDTH - wordsLabel.getWidth() - 52f, HEIGHT - 200f);
        wordsLabel.setFontScale(0.9f);

        lastSentenceLabel = new Label("", labelStyle);
        lastSentenceLabel.setText(
                gameManager.playerLives <= 0
                        ? "And she hates you!" : "And you live happily ever after..."
        );
        lastSentenceLabel.setFontScale(0.8f);
        lastSentenceLabel.setPosition(20f, 120f);
        lastSentenceLabel.setVisible(false);

        stage.addActor(congratulationsLabel);
        stage.addActor(wordsLabel);
        stage.addActor(lastSentenceLabel);
        stage.addActor(bombermanAnimImage);
        stage.addActor(princessAnimImage);

        stage.addAction(
                Actions.sequence(
                        Actions.delay(5f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                lastSentenceLabel.setVisible(true);
                            }
                        }),
                        Actions.delay(5f),
                        Actions.fadeOut(1f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                if (gameManager.playerLives > 0) {
                                    game.setScreen(new mainMenuDP(game));
                                } else {
                                    game.setScreen(new mainMenuDP(game));
                                }
                            }
                        })
                )
        );

        gameManager.getInstance().playMusic("StageCleared.ogg", false);
    }

    @Override
    public void render(float f) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(f);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        font.dispose();
        stage.dispose();
    }
}
/* Final */
