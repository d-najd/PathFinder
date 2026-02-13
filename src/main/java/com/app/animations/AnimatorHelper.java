package com.app.animations;

import com.app.data.Piece;
import com.app.Settings;

import java.awt.*;

public class AnimatorHelper {

    /**
     * calculates center of a given piece, the piece has 0 width and height
     *
     * @return a rectangle in the spot of the center of the piece with 0 width and height
     */
    public static Rectangle calculateCenter(Piece p) {
        return new Rectangle((p.getX() * Settings.RECT_WID) + Settings.RECT_WID / 2,
                (p.getY() * Settings.RECT_WID) + Settings.RECT_WID / 2, 0, 0);
    }

    /**
     * calculates the position that the piece will be after the animation has ended, uses #Settings.RECT_WID as width and height
     *
     * @return a rectangle where the piece needs to be
     */
    public static Rectangle calculateEndPos(Piece p) {
        return new Rectangle(p.getX() * Settings.RECT_WID,
                p.getY() * Settings.RECT_WID, Settings.RECT_WID, Settings.RECT_WID);
    }
}
