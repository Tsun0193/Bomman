package com.bomman.game.display;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bomman.game.BGame;

public class playDP extends ScreenAdapter {
    private final BGame bGame;
    private final SpriteBatch batch;
    private FitViewport viewport;
    private OrthographicCamera camera;

    /**
     * Constructor.
     *
     * @param bGame bGame
     */
    public playDP(BGame bGame) {
        this.bGame = bGame;
        this.batch = bGame.getBatch();
    }


    public void show() {
        camera = new OrthographicCamera();

    }
}
