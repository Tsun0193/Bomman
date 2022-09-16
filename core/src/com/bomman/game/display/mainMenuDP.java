package com.bomman.game.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
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


        stage.addActor(background);
        gameManager.getInstance().playMusic("SuperBomberman-Title.ogg", true);
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
