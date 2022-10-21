package com.bomman.game.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class renderer extends Component {
    private Sprite sprite;

    public renderer() {

    }
    /**
     * Constructor.
     *
     * @param region TextureRegion
     */
    public renderer(TextureRegion region) {
        sprite = new Sprite(region);
    }

    /**
     * Extended Constructor.
     *
     * @param region TextureRegion
     * @param w      width
     * @param h      height
     */
    public renderer(TextureRegion region, float w, float h) {
        this(region);
        sprite.setSize(w, h);
    }


    //Setter Methods.
    public void setSpriteRegion(TextureRegion region) {
        sprite.setRegion(region);
    }

    public void setSpriteOrigin(float x, float y) {
        sprite.setOrigin(x, y);
    }

    public void setSpritePos(float x, float y) {
        sprite.setPosition(x, y);
    }

    public void setSpriteSize(float x, float y) {
        sprite.setSize(x, y);
    }

    public void setSpriteRotation(float r) {
        sprite.setRotation(r);
    }

    public void setSpriteScale(float x, float y) {
        sprite.setScale(x, y);
    }

    public void setSpriteFlip(boolean a, boolean b) {
        sprite.setFlip(a, b);
    }

    public void setSpriteColor(Color color) {
        sprite.setColor(color);
    }

    public void spriteDraw(SpriteBatch batch) {
        sprite.draw(batch);
    }


    //Getter Methods.
    public boolean flipX() {
        return sprite.isFlipX();
    }

    public boolean flipY() {
        return sprite.isFlipY();
    }

}
/* Final */
