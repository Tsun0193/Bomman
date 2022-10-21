package com.bomman.game.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class anima extends Component {
    private HashMap<String, Animation> animas;

    public anima() {
        this.animas = new HashMap<String, Animation>();
    }

    public anima(HashMap<String, Animation> map) {
        this.animas = map;
    }

    public void setAnimas(String state, Animation anima) {
        this.animas.put(state, anima);
    }

    public Animation getAnimas(String state) {
        return this.animas.get(state);
    }

    public TextureRegion getTextureRegion(String state, float time){
        Animation a = this.animas.get(state);
        return (TextureRegion) a.getKeyFrame(time);
    }

    public TextureRegion getTextureRegion(String state, float time, boolean looping) {
        return (TextureRegion) animas.get(state).getKeyFrame(time, looping);
    }
}
/* Final. */
