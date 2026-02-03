package com.app.mazes;

import com.app.Settings;
import com.app.data.Piece;
import com.app.ui.DrawGrid;

import java.util.ArrayList;

public class RandomMaze implements IMaze {
    @Override
    public Maze currentMaze() {
        return Maze.Random;
    }

    @Override
    public void generateMaze(ArrayList<ArrayList<Piece>> grid, DrawGrid gridObj) {
        for (ArrayList<Piece> colPieceArr : grid) {
            for (Piece curPiece : colPieceArr) {
                assert curPiece.getType() == Piece.Type.Empty || curPiece.getType() == Piece.Type.End || curPiece.getType() == Piece.Type.Start;
                if (curPiece.getType() == Piece.Type.Start || curPiece.getType() == Piece.Type.End) {
                    continue;
                }

                double WALL_PERCENTAGE_UNORM = 0.3d;
                if (WALL_PERCENTAGE_UNORM < Math.random()) {
                    continue;
                }

                curPiece.setType(Piece.Type.Wall);
            }
            gridObj.piecesForRepainting.addAll(colPieceArr);
        }

        gridObj.paintImmediately(0, 0, Settings.GRID_WID * Settings.RECT_WID,
                Settings.GRID_HEI * Settings.RECT_WID);
    }
}