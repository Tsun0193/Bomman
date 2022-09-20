package com.bomman.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Disposable;
import com.bomman.game.game.gameManager;

public class hud implements Disposable {
    private final SpriteBatch batch;
    private TextureAtlas textureAtlas;
    private Texture bgTexture;
    private Texture bombTimerTexture;

    private Sprite bombSprite;
    private Sprite bombTimerSprite;
    private Sprite powerSprite;
    private Sprite speedSprite;
    private Sprite remoteSprite;
    private Sprite bommanSprite;

    private Label fpsLabel;
    private Label lvLabel;
    private Label livesLabel;
    private Label xLabel;
    private Label zLabel;

    private boolean showFPS = false;
    private StringBuilder stringBuilder;


    public hud(SpriteBatch batch, float width, float height){
        this.batch = batch;
        AssetManager assetManager = gameManager.getInstance().getAssetManager();
        textureAtlas = assetManager.get("img/actors.pack", TextureAtlas.class);
        bombSprite = new Sprite(new TextureRegion(textureAtlas.findRegion("Bomb"), 0, 0, 16, 16));
        bombSprite.setBounds(15.0f, 11.5f, 1, 1);

        Pixmap pixmap = new Pixmap(5, 15, Pixmap.Format.RGBA8888);
        pixmap.setColor(240.0f / 255.0f, 128 / 255.0f, 0, 1.0f);
        pixmap.fill();

        bgTexture = new Texture(pixmap);

        pixmap.setColor(1, 1, 1, 1);
        pixmap.fill();
        bombTimerTexture = new Texture(pixmap);
        pixmap.dispose();

        bombTimerSprite = new Sprite(bombTimerTexture);

        stringBuilder = new StringBuilder();
    }

    public void draw(float f) {
        handleInput();
        batch.begin();
        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            showFPS = !showFPS;
            fpsLabel.setVisible(showFPS);
        }
    }

    @Override
    public void dispose() {
        bgTexture.dispose();
        bombTimerTexture.dispose();
    }

    public void setLevelInfo(int lv) {
        lvLabel.setText("Level: " + lv);
    }
}
