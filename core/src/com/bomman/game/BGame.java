package com.bomman.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bomman.game.game.gameManager;
import com.bomman.game.display.mainMenuDP;

public class BGame extends Game {
    private SpriteBatch batch;

    public SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new mainMenuDP(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        gameManager.getInstance().getAssetManager().dispose();
    }
}
