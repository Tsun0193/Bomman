package com.bomman.game.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class transform extends Component {
    public float xPos;
    public float yPos;
    public float xScale;
    public float yScale;

    public float temp;
    public float degree;

    /**
     * Constructor.
     *
     * @param xP X Position
     * @param yP Y Position
     * @param xS X Scale
     * @param yS Y Scale
     * @param d  Degree
     */
    public transform(float xP, float yP, float xS, float yS, float d) {
        this.xPos = xP;
        this.yPos = yP;
        temp = yPos;

        this.xScale = xS;
        this.yScale = yS;
        this.degree = d;
    }


    /**
     * Default Constructor.
     */
    public transform() {
        this(0, 0, 1, 1, 0);
    }


    /**
     * Positions Setter.
     *
     * @param x float
     * @param y float
     */
    public void setPos(float x, float y) {
        xPos = x;
        yPos = y;
        temp = yPos;
    }


    /**
     * Vector-type variable Positions Setter.
     *
     * @param pos
     */
    public void setPos(Vector2 pos) {
        xPos = pos.x;
        yPos = pos.y;
        temp = yPos;
    }
}
