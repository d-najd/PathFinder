package com.app.data;

import com.app.Settings;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Piece {
    protected int x = 0;
    protected int y = 0;
    protected Type type = Type.Empty;
    // the piece type that the piece starts from, if the algorithm is bidirectional it will
    // have 2 start types (start piece and end piece)
    protected Type startType;

    public Piece(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Rectangle2D getRect() {
        return new Rectangle2D.Double(x * Settings.RECT_WID, y * Settings.RECT_WID, Settings.RECT_WID, Settings.RECT_WID);
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
        assert startType == Type.Start || startType == Type.End;
        this.startType = type;
    }

    private static Color getColor(Type type){
        return switch (type) {
            case Empty -> Color.white;
            case Wall -> Color.darkGray;
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
