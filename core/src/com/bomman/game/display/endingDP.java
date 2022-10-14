package com.bomman.game.display;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bomman.game.BGame;
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
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        font.dispose();
        stage.dispose();
    }
}
