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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bomman.game.BGame;
import com.bomman.game.game.gameManager;


public class mainMenuDP extends ScreenAdapter {
    private final BGame bGame;
    private final SpriteBatch batch;
    private FitViewport viewport;
    private Stage stage;
    private BitmapFont font;
    private Texture backgroundTexture;
    private Texture indicatorTexture;
    private int currentSelection;
    private boolean selected;
    private float xIndicator;
    private float yIndicator;
    private Image indicator1;
    private Image indicator2;
    private Image indicator;

    public mainMenuDP(BGame bGame) {
        this.bGame = bGame;
        this.batch = bGame.getBatch();
    }

    @Override
    public void show() {
        viewport = new FitViewport(640, 480);
        stage = new Stage(viewport, batch);
        font = new BitmapFont(Gdx.files.internal("fonts/foo.fnt"));

        Label.LabelStyle style;
        style = new Label.LabelStyle();
        style.font = this.font;
        style.fontColor = Color.WHITE;
        Label title = new Label("Bomman", style);
        title.setFontScale(1.6f);
        title.setPosition(140,360);

        Pixmap pixmap = new Pixmap(640, 480, Pixmap.Format.RGB888);
        pixmap.setColor(240/255.0f, 128/255.0f, 0, 1.0f);
        pixmap.fill();
        backgroundTexture = new Texture(pixmap);
        pixmap.dispose();
        Image background = new Image(backgroundTexture);

        xIndicator = 160.0f;
        yIndicator = 240.0f;

        TextureAtlas textureAtlas = gameManager.getInstance().getAssetManager().get("img/actors.pack", TextureAtlas.class);
        indicator1 = new Image(new TextureRegion(textureAtlas.findRegion("MainMenuLogo"), 40,0,40,26));
        indicator1.setSize(80.0f, 52.0f);
        indicator1.setPosition(xIndicator, yIndicator);

        indicator2 = new Image(new TextureRegion(textureAtlas.findRegion("MainMenuLogo"),40,0,40,26));
        indicator2.setSize(80.0f, 52.0f);
        indicator2.setPosition(xIndicator, yIndicator);
        indicator2.setVisible(false);

        indicatorTexture = new Texture("img/indications.png");
        indicator = new Image(indicatorTexture);
        indicator.setPosition(640.0f - indicator.getWidth() - 12.0f, 12.0f);

        stage.addActor(background);
        stage.addActor(indicator);
        stage.addActor(title);
        stage.addActor(indicator1);
        stage.addActor(indicator2);
        currentSelection = 0;
        selected = false;
        gameManager.getInstance().playMusic("SuperBomberman-Title.ogg", true);
    }

    public void handleInput() {
        if(!selected) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
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

            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
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

            if (Gdx.input.isKeyJustPressed(Input.Keys.X) || Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                gameManager.getInstance().playSound("Teleport.ogg");

                selected = true;

                indicator1.setVisible(false);
                indicator2.setVisible(true);

                RunnableAction action = new RunnableAction();
                action.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        switch (currentSelection) {
                            case 0:
                            case 1:
                                gameManager.difficultyRespawn(true, true);
                                break;
                            case 2:
                                gameManager.difficultyRespawn(false, true);
                                break;
                            default:
                                gameManager.difficultyRespawn(true, false);
                                break;
                        }
                        gameManager.playerLives = 3;
                        bGame.setScreen(new playDP(bGame, 1));
                    }
                });

                stage.addAction(new SequenceAction(Actions.delay(0.2f), Actions.fadeOut(1f), action));
            }
        }
    }

    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int w, int h) {
        viewport.update(w, h);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
    }

}
