package com.bomman.game.checkpoint;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class checkpoint{
    public Preferences prefs;

    /**
     * <p>
     *     Univariate Constructor with String type Variable.
     * </p>
     * <p>
     *     Clock: 0 - never used;
     *     1 - activated
     * </p>
     * @param dir directory
     */
    public checkpoint(String dir) {
        prefs = Gdx.app.getPreferences(dir);
        prefs.putInteger("clock", 0);
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
