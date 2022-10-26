package com.bomman.game.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class anima extends Component {
    private HashMap<String, Animation<TextureRegion>> animas;

    public anima() {
        this.animas = new HashMap<>();
    }

    public anima(HashMap<String, Animation<TextureRegion>> map) {
        this.animas = map;
    }

    public void setAnimas(String state, Animation<TextureRegion> anima) {
        this.animas.put(state, anima);
    }

    public Animation<TextureRegion> getAnimas(String state) {
        return this.animas.get(state);
    }

    public TextureRegion getTextureRegion(String state, float time){
        Animation<TextureRegion> a = this.animas.get(state);
        return a.getKeyFrame(time);
    }

    public TextureRegion getTextureRegion(String state, float time, boolean looping) {
        return animas.get(state).getKeyFrame(time, looping);
    }
}
/* Final. */
