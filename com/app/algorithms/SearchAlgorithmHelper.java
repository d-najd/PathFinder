package com.app.algorithms;

import com.app.data.Piece;

import java.util.ArrayList;

public class SearchAlgorithmHelper {
    public static int[] dx = {1, -1, 0, 0}; //right, left, NA, NA
    public static int[] dy = {0, 0, 1, -1}; //NA, NA, bottom, top

    public static Piece getPieceByIndex(ArrayList<ArrayList<Piece>> grid, Piece dequeuedPiece, int i) {
        var xc = dequeuedPiece.getX() + SearchAlgorithmHelper.dx[i];
        var yc = dequeuedPiece.getY() + SearchAlgorithmHelper.dy[i];

        if (xc < 0 || xc >= grid.size() || yc < 0 || yc >= grid.getFirst().size()) {
            return null;
        }

        return grid.get(xc).get(yc);
    }
}
