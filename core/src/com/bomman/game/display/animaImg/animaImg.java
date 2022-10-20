package com.bomman.game.display.animaImg;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.HashMap;

public class animaImg extends Image {
    private HashMap<String, Animation> anims;

    private String current;
    private float stateTime;

    public animaImg(TextureRegion region) {
        super(region);
        anims = new HashMap<>();
        stateTime = 0;
        current = "";
    }

    public void put(String key, Animation anim) {
        anims.put(key, anim);
        setCurrent(key);
    }

    public void setCurrent(String key) {
        if (key.equals(current)) {
            return;
        }
        current = key;
        stateTime = 0;
    }

    public void reset() {
        stateTime = 0;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        setDrawable(new TextureRegionDrawable((Texture) anims.get(current).getKeyFrame(stateTime)));
    }
}
/* Final */
