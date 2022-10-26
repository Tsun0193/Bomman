package com.bomman.game.checkpoint;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class checkpoint{
    public Preferences prefs;
    public checkpoint(String dir) {
        prefs = Gdx.app.getPreferences(dir);
    }

    public void setInt(String name, int value) {
        prefs.putInteger(name, value);
        prefs.flush();
        Gdx.app.log(name, String.valueOf(value));
    }

    public int getInt(String name) {
        Gdx.app.log(name, String.valueOf(prefs.getInteger("name")));
        return prefs.getInteger(name);
    }

}
