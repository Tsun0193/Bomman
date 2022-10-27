package com.bomman.game.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bomman.game.BGame;
import com.bomman.game.checkpoint.checkpoint;
import com.bomman.game.game.gameManager;

import java.io.File;


public class mainMenuDP extends ScreenAdapter {
    private final BGame bGame;
    private final SpriteBatch batch;
    private FitViewport viewport;
    private Stage stage;
    private BitmapFont font;
    private Texture backgroundTexture;
    private Texture indicatorTexture;
    private int currentSelection;
    private int currentLoaded;
    private boolean selected;
    private boolean loaded;
    private float xIndicator;
    private float yIndicator;
    private Image indicator1;
    private Image indicator2;
    private Label.LabelStyle style;
    private Image indicator;
    private Label title;

    public mainMenuDP(BGame bGame) {
        this.bGame = bGame;
        this.batch = bGame.getBatch();
    }

    @Override
    public void show() {
        viewport = new FitViewport(640, 480);
        stage = new Stage(viewport, batch);
        font = new BitmapFont(Gdx.files.internal("fonts/foo.fnt"));

        style = new Label.LabelStyle();
        style.font = this.font;
        style.fontColor = Color.WHITE;
        title = new Label("Bomman", style);
        title.setFontScale(1.6f);
        title.setPosition(140, 360);

        Pixmap pixmap = new Pixmap(640, 480, Pixmap.Format.RGB888);
        pixmap.setColor(240 / 255.0f, 128 / 255.0f, 0, 1.0f);
        pixmap.fill();
        backgroundTexture = new Texture(pixmap);
        pixmap.dispose();
        Image background = new Image(backgroundTexture);

        xIndicator = 160.0f;
        yIndicator = 240.0f;

        TextureAtlas textureAtlas = gameManager.getInstance().getAssetManager().get("img/actors.pack", TextureAtlas.class);
        indicator1 = new Image(new TextureRegion(textureAtlas.findRegion("MainMenuLogo"), 40, 0, 40, 26));
        indicator1.setSize(80.0f, 52.0f);
        indicator1.setPosition(xIndicator, yIndicator);

        indicator2 = new Image(new TextureRegion(textureAtlas.findRegion("MainMenuLogo"), 40, 0, 40, 26));
        indicator2.setSize(80.0f, 52.0f);
        indicator2.setPosition(xIndicator, yIndicator);
        indicator2.setVisible(false);

        indicatorTexture = new Texture("img/indications.png");
        indicator = new Image(indicatorTexture);
        indicator.setPosition(640.0f - indicator.getWidth() - 12.0f, 12.0f);

        Label continueLabel = new Label("Continue", style);
        continueLabel.setPosition((640 - continueLabel.getWidth()) / 2 + 5, 240);

        Label newGameLabel = new Label("New Game", style);
        newGameLabel.setPosition((640 - newGameLabel.getWidth()) / 2 + 17, 180);

        stage.addActor(background);
        stage.addActor(indicator);
        stage.addActor(title);
        stage.addActor(continueLabel);
        stage.addActor(newGameLabel);
        stage.addActor(indicator1);
        stage.addActor(indicator2);
        currentSelection = 0;
        currentLoaded = 0;
        selected = false;
        loaded = false;

        gameManager.getInstance().playMusic("SuperBomberman-Title.ogg", true);
    }

    public void handleInput() {
        if (!loaded && !selected && (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN))) {
            gameManager.getInstance().playSound("Pickup.ogg");

            currentLoaded = 1 - currentLoaded;
            float yIndicator2 = yIndicator - currentLoaded * 60.0f;

            MoveToAction action = new MoveToAction();
            action.setPosition(xIndicator, yIndicator2);
            action.setDuration(0.2f);
            indicator1.clearActions();
            indicator1.addAction(action);
            indicator2.setPosition(xIndicator, yIndicator2);
        }

        if (!loaded && !selected && Gdx.input.isKeyJustPressed(Input.Keys.X) || Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            loaded = true;
            selected = false;
            Pixmap pixmap = new Pixmap(640, 480, Pixmap.Format.RGB888);
            pixmap.setColor(240 / 255.0f, 128 / 255.0f, 0, 1.0f);
            pixmap.fill();
            backgroundTexture = new Texture(pixmap);
            pixmap.dispose();
            Image background = new Image(backgroundTexture);
            stage.addActor(background);
            stage.clear();
            style = new Label.LabelStyle();
            style.font = this.font;
            style.fontColor = Color.WHITE;

            title = new Label("Bomman", style);
            title.setFontScale(1.6f);
            title.setPosition(140, 360);

            xIndicator = 160.0f;
            yIndicator = 240.0f;
            indicator1.setPosition(xIndicator, yIndicator);
            indicator2.setPosition(xIndicator, yIndicator);

            if (currentLoaded != 0) { //New Game
                gameManager.getInstance().playSound("Pickup.ogg");
                indicatorTexture = new Texture("img/newGameIndications.png");
                indicator = new Image(indicatorTexture);
                indicator.setPosition(640.0f - indicator.getWidth() - 12.0f, 3.0f);

                indicator1.setPosition(xIndicator, yIndicator);
                indicator2.setPosition(xIndicator, yIndicator);

                Label easyLabel = new Label("Easy", style);
                easyLabel.setPosition((640 - easyLabel.getWidth()) / 2, 240);

                Label normalLabel = new Label("Normal", style);
                normalLabel.setPosition((640 - normalLabel.getWidth()) / 2, 180);

                Label hardLabel = new Label("Hard", style);
                hardLabel.setPosition((640 - hardLabel.getWidth()) / 2, 120);

                currentSelection = 0;

                stage.addActor(background);
                stage.addActor(indicator);
                stage.addActor(title);
                stage.addActor(easyLabel);
                stage.addActor(normalLabel);
                stage.addActor(hardLabel);
                stage.addActor(indicator1);
                stage.addActor(indicator2);
            } else {
                gameManager.getInstance().playSound("Teleport.ogg");

                File file = new File("C:/Users/Admin/.prefs/Untitled Save");
                if (!file.exists()) {
                    System.out.println("No Saving Progress Existed");
                    bGame.setScreen(new mainMenuDP(bGame));
                } else {
                    final checkpoint store = new checkpoint("Untitled Save");
                    if (store.prefs.getInteger("clock") == 0) { // created but not edited.
                        System.out.println("No Saving Progress Existed");
                        bGame.setScreen(new mainMenuDP(bGame));
                    } else {
                        gameManager.difficultyRespawn(store.prefs.getBoolean("infLives"), store.prefs.getBoolean("reset"));
                        gameManager.loadCheckpoint(store);

                        indicator1.setVisible(false);
                        indicator2.setVisible(true);

                        if (!store.prefs.getBoolean("gameOver")) {
                            System.out.println("No Saving Progress Saved");
                            bGame.setScreen(new mainMenuDP(bGame));
                        }
                        RunnableAction action = new RunnableAction();
                        action.setRunnable(new Runnable() {
                            @Override
                            public void run() {
                                bGame.setScreen(new playDP(bGame, store.prefs.getInteger("level")));
                            }
                        });

                        stage.addAction(new SequenceAction(Actions.delay(0.2f), Actions.fadeOut(1f), action));
                    }
                }
            }
        }


        if (loaded && !selected && Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            currentSelection--;
            gameManager.getInstance().playSound("Pickup.ogg");

            if (currentSelection < 0) {
                currentSelection += 3;
            }

            float yIndicator2 = yIndicator - currentSelection * 60.0f;

            MoveToAction action = new MoveToAction();
            action.setPosition(xIndicator, yIndicator2);
            action.setDuration(0.2f);
            indicator1.clearActions();
            indicator1.addAction(action);
            indicator2.setPosition(xIndicator, yIndicator2);
        }

        if (loaded && !selected && Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            currentSelection++;
            gameManager.getInstance().playSound("Pickup.ogg");

            if (currentSelection >= 3) {
                currentSelection -= 3;
            }

            float yIndicator2 = yIndicator - currentSelection * 60.f;

            MoveToAction action = new MoveToAction();
            action.setPosition(xIndicator, yIndicator2);
            action.setDuration(0.2f);
            indicator1.clearActions();
            indicator1.addAction(action);
            indicator2.setPosition(xIndicator, yIndicator2);
        }

        if (loaded && !selected && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            gameManager.getInstance().playSound("Teleport.ogg");

            indicator1.setVisible(false);
            indicator2.setVisible(true);

            RunnableAction action = new RunnableAction();
            action.setRunnable(new Runnable() {
                @Override
                public void run() {
                    gameManager.initPlayerAttributes();
                    checkpoint Store = new checkpoint("Untitled Save");
                    Store.prefs.putInteger("clock", 1);
                    gameManager.initCheckpoint(Store);
//                    System.out.println(currentSelection);

                    switch (currentSelection) {
                        case 0:
                            gameManager.difficultyRespawn(true, false);
                            gameManager.difficultyCheckpoint(Store, true, false);
//                            System.out.println("Easy");
                            break;
                        case 1:
                            gameManager.difficultyRespawn(false, false);
                            gameManager.difficultyCheckpoint(Store, false, false);
//                            System.out.println("Normal");
                            break;
                        case 2:
                            gameManager.difficultyRespawn(false, true);
                            gameManager.difficultyCheckpoint(Store, false, true);
//                            System.out.println("Hard");
                            break;
                    }

                    bGame.setScreen(new playDP(bGame, 1));
                }
            });

            stage.addAction(new SequenceAction(Actions.delay(0.2f), Actions.fadeOut(1f), action));
        }
    }

    @Override
    public void render(float f) {
        handleInput();

        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(f);
        stage.draw();
    }

    @Override
    public void resize(int w, int h) {
        viewport.update(w, h);
    }

    @Override
    public void hide() {
        gameManager.getInstance().stopMusic();
        dispose();
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        indicatorTexture.dispose();
        stage.dispose();
        font.dispose();
    }

}
/* Final */
