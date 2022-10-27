package com.bomman.game.checkpoint;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.bomman.game.game.gameManager;

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
    }

    public void initCheckpoint() {
        prefs.putInteger("level", gameManager.level);
        prefs.putInteger("playerLives", 3);
        prefs.putInteger("playerMaxSpeed", 3);
        prefs.putInteger("playerBombCount", 1);
        prefs.putInteger("playerBombPow", 1);
        prefs.putBoolean("playerBombInteractablity", false);
        prefs.putBoolean("playerRemoteBomb", false);
        prefs.putBoolean("gameOver", gameManager.gameOver);
        prefs.putFloat("playerBombGenerateTime", 2.0f);
        prefs.flush();
    }
}
