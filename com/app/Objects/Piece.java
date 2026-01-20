package com.app.Objects;

import com.app.Settings;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Piece {
    private int x = 0;
    private int y = 0;
    private Type type = Type.Empty;
    private Type startType;

    public Piece(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Rectangle2D getRect() {
        var rectWid = Settings.RECT_WID;
        var rectHei = rectWid;
        var rectX = rectWid;
        var rectY = rectHei;

        return new Rectangle2D.Double(x * rectX, y * rectY, rectWid, rectHei);
    }

    public Color getColor() {
        return getColor(this.type);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getStartType() {
        return startType;
    }

    public void setStartType(Type type) {
        this.startType = type;
    }

    private static Color getColor(Type type){
        /** type used for differentiating the type of the current cell
         *             type 0: empty cell (default value), white
         *             type 1: wall, darkGray
         *             type 2: start, red
         *             type 3: end, darkGreen
         *             type 4: already checked, lightBlue
         *             type 5: in queue for checking, darkBlue
         *             type 6: for displaying shortest path, yellow
         */

        switch (type) {
            case Empty:
                return Color.white;
            case Wall:
                return new Color(64, 64, 64);
            case Start:
                return Color.red;
            case End:
                return new Color(0,128,0);
            case Checked:
                return Color.blue;
            case Queue:
                return new Color(0, 0, 128);
            case DisplayingPath:
                return Color.yellow;
            default:
                System.out.println("[ERROR] the current type of cell " + type + " is not defined in code");
                break;
        }
        return Color.magenta;
    }

    public enum Type {
        Empty,
        Wall,
        Start,
        End,
        Checked,
        Queue,
        DisplayingPath,

        /*
         *             type 0: empty cell (default value), white
         *             type 1: wall, darkGray
         *             type 2: start, red
         *             type 3: end, darkGreen
         *             type 4: already checked, lightBlue
         *             type 5: in queue for checking, darkBlue
         *             type 6: for displaying shortest path, yellow
         */
    }

    //TODO add the different types here and a function that returns the color of each and something else if needed
}
