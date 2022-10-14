package com.bomman.game.components;

import com.artemis.Component;
import com.bomman.game.game.gameManager;

public class explosion extends Component {
    public static short defaultMaskBits = gameManager.PLAYER_BIT |
            gameManager.BOMB_BIT |
            gameManager.ENEMY_BIT |
            gameManager.BREAKABLE_BIT;
}
