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
        return switch (type) {
            case Empty -> Color.white;
            case Wall -> new Color(64, 64, 64);
            case Start -> Color.red;
            case End -> new Color(0, 128, 0);
            case Checked -> Color.blue;
            case InQueue -> new Color(0, 0, 128);
            case DisplayingPath -> Color.yellow;
        };
    }

    public enum Type {
        Empty,
        Wall,
        Start,
        End,
        Checked,
        InQueue,
        DisplayingPath,
    }
}
