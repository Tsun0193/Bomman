package com.bomman.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bomman.game.components.character;
import com.bomman.game.game.gameManager;

public class hud implements Disposable {
    private final SpriteBatch batch;
    private TextureAtlas textureAtlas;
    private Texture bgTexture;
    private Texture bombTimerTexture;
    private BitmapFont font;

    private Sprite bombSprite;
    private Sprite bombTimerSprite;
    private Sprite powerSprite;
    private Sprite speedSprite;
    private Sprite remoteSprite;
    private Sprite kickSprite;
    private Sprite bommanSprite;
    private Animation bommanAnimation;

    private Stage stage;
    private Label fpsLabel;
    private Label lvLabel;
    private Label livesLabel;
    private Label xLabel;
    private Label zLabel;

    private boolean showFPS = false;
    private StringBuilder stringBuilder;
    private final float leftAlignment = 15.5f;
    private final float SCALE = 16.0f;
    private float stateTime;


    public hud(SpriteBatch batch, float width, float height) {
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
        bombTimerSprite.setBounds(16.0f, 12.5f, 3.0f, 0.2f);

        lvLabel = new Label("Level", new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/foo.fnt")), Color.WHITE));
        lvLabel.setPosition(15.5f * SCALE, 3.0f * SCALE);
        lvLabel.setFontScale(0.4f);
        stringBuilder = new StringBuilder();

        TextureRegion itemTexture = textureAtlas.findRegion("Items");
        powerSprite = new Sprite(new TextureRegion(itemTexture, 16 * 1, 0, 16, 16));
        powerSprite.setBounds(leftAlignment, 9.0f, 1, 1);

        speedSprite = new Sprite(new TextureRegion(itemTexture, 16 * 2, 0, 16, 16));
        speedSprite.setBounds(leftAlignment, 8.0f, 1, 1);

        kickSprite = new Sprite(new TextureRegion(itemTexture, 16 * 3, 0, 16, 16));
        kickSprite.setBounds(leftAlignment, 7.0f, 1, 1);

        remoteSprite = new Sprite(new TextureRegion(itemTexture, 16 * 4, 0, 16, 16));
        remoteSprite.setBounds(leftAlignment, 6.0f, 1, 1);

        Array<TextureRegion> keyFrames = new Array<>();
        for (int i = 0; i <= 4; i++) {
            keyFrames.add(new TextureRegion(textureAtlas.findRegion("Bomberman_big"), 32 * i, 0, 32, 48));
        }
        bommanAnimation = new Animation(0.2f, keyFrames, Animation.PlayMode.LOOP_PINGPONG);
        bommanSprite = new Sprite((Texture) bommanAnimation.getKeyFrame(0));
        bommanSprite.setBounds(17.5f, 0.5f, 2f, 3f);
        stateTime = 0;

        FitViewport viewport = new FitViewport(width * SCALE, height * SCALE);
        stage = new Stage(viewport, batch);
        font = new BitmapFont(Gdx.files.internal("fonts/foo.fnt"));
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        fpsLabel = new Label("FPS:", labelStyle);
        fpsLabel.setFontScale(0.3f);
        fpsLabel.setPosition(16.0f * SCALE, -0.8f * SCALE);
        fpsLabel.setVisible(showFPS);

        livesLabel = new Label("" + gameManager.playerLives, labelStyle);
        livesLabel.setFontScale(0.5f);
        livesLabel.setPosition(16.8f * SCALE, 12.8f * SCALE);

        Image bombermanImage = new Image(new TextureRegion(textureAtlas.findRegion("Items"), 16 * 5, 0, 16, 16));
        bombermanImage.setPosition(leftAlignment * SCALE, 13.5f * SCALE);

        xLabel = new Label("X", labelStyle);
        xLabel.setFontScale(0.4f);
        xLabel.setPosition(16.8f * SCALE, 6.3f * SCALE);

        zLabel = new Label("Z", labelStyle);
        zLabel.setFontScale(0.4f);
        zLabel.setPosition(16.8f * SCALE, 5.3f * SCALE);

        stage.addActor(fpsLabel);
        stage.addActor(lvLabel);
        stage.addActor(livesLabel);
        stage.addActor(bombermanImage);
        stage.addActor(xLabel);
        stage.addActor(zLabel);
    }

    public void draw(float f) {
        handleInput();

        stateTime += f;
        bommanSprite.setRegion((Texture) bommanAnimation.getKeyFrame(stateTime));

        if (gameManager.playerRemoteBomb) {
            if (gameManager.playerBombPow + 1 < character.MAX_BOMB_POWER) {
                bombSprite.setRegion(new TextureRegion(textureAtlas.findRegion("Bomb"), 16 * 3, 0, 16, 16));
            } else {
                bombSprite.setRegion(new TextureRegion(textureAtlas.findRegion("Bomb"), 16 * 3, 16 * 1, 16, 16));
            }
        } else {
            if (gameManager.playerBombPow + 1 < character.MAX_BOMB_POWER) {
                bombSprite.setRegion(new TextureRegion(textureAtlas.findRegion("Bomb"), 0, 0, 16, 16));
            } else {
                bombSprite.setRegion(new TextureRegion(textureAtlas.findRegion("Bomb"), 0, 16 * 1, 16, 16));
            }
        }

        batch.begin();
        batch.draw(bgTexture, 15.0f, 0.0f);
        for (int i = 0; i < gameManager.playerBombCount; i++) {
            float alpha;
            bombSprite.setPosition(15.0f + i % 5.0f, 11.5f - i / 5.0f);
            alpha = i >= gameManager.playerBombRemaining ? 0.5f : 1.0f;
            bombSprite.draw(batch, alpha);
        }
        bombTimerSprite.setSize((1.0f - gameManager.playerBombGenerateTimeLeft / gameManager.playerBombGenerateTime) * 3.0f, 0.2f);
        bombTimerSprite.draw(batch);

        if (gameManager.playerBombPow > 0) {
            for (int i = 0; i < gameManager.playerBombPow; i++) {
                powerSprite.setPosition(leftAlignment + i * 0.5f, 9.0f);
                powerSprite.draw(batch);
            }

        } else {
            powerSprite.setPosition(leftAlignment, 9.0f);
            powerSprite.draw(batch, 0.5f);
        }

        if (gameManager.playerMaxSpeed > 0) {
            for (int i = 0; i < gameManager.playerMaxSpeed; i++) {
                speedSprite.setPosition(leftAlignment + i * 0.5f, 8.0f);
                speedSprite.draw(batch);
            }
        } else {
            speedSprite.setPosition(leftAlignment, 8.0f);
            speedSprite.draw(batch, 0.5f);
        }

        kickSprite.draw(batch, gameManager.playerKickBomb ? 1.0f : 0.5f);
        remoteSprite.draw(batch, gameManager.playerRemoteBomb ? 1.0f : 0.5f);

        bommanSprite.draw(batch);

        batch.end();

        xLabel.setVisible(gameManager.playerKickBomb);
        zLabel.setVisible(gameManager.playerRemoteBomb);

        livesLabel.setText("" + gameManager.playerLives);

        if (showFPS) {
            stringBuilder.setLength(0);
            stringBuilder.append("FPS:").append(Gdx.graphics.getFramesPerSecond());
            fpsLabel.setText(stringBuilder.toString());
        }
        stage.draw();
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
        font.dispose();
        stage.dispose();
    }

    public void setLevelInfo(int lv) {
        lvLabel.setText("Level: " + lv);
    }
}
/* Final */