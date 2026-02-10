package com.app.mazes;

import java.util.List;

import com.app.Settings;
import com.app.data.Piece;
import com.app.ui.DrawGrid;

class MazeUtils {
	public static boolean isEmptyPiece(Piece curPiece) {
		assert curPiece.getType() == Piece.Type.Empty || curPiece.getType() == Piece.Type.End
				|| curPiece.getType() == Piece.Type.Start;

		if (curPiece.getType() == Piece.Type.Start || curPiece.getType() == Piece.Type.End) {
			return false;
		}
		return true;
	}

	public static void fillBorders(List<List<Piece>> grid, DrawGrid gridObj) {
		for (Piece curPiece : grid.get(0)) {
			curPiece.setType(Piece.Type.Wall);
		}

		/*
		 * for (List<Piece> colPieceArr : grid) {
		 * colPieceArr.get(0).setType(Piece.Type.Wall);
		 * colPieceArr.get(colPieceArr.size() - 1).setType(Piece.Type.Wall);
		 * }
		 */

		/*
		 * for (Piece curPiece : grid.get(grid.size() - 1)) {
		 * curPiece.setType(Piece.Type.Wall);
		 * }
		 */
	}

	public static void repaintAll(List<List<Piece>> grid, DrawGrid gridObj) {
		for (List<Piece> colPieceArr : grid) {
			for (Piece curPiece : colPieceArr) {
				if (!MazeUtils.isEmptyPiece(curPiece)) {
					continue;
				}

				double WALL_PERCENTAGE_UNORM = 0.275d;
				if (WALL_PERCENTAGE_UNORM < Math.random()) {
					continue;
				}

				curPiece.setType(Piece.Type.Wall);
			}
			gridObj.piecesForRepainting.addAll(colPieceArr);
		}

		gridObj.paintImmediately(0, 0, Settings.GRID_WID * Settings.RECT_WID, Settings.GRID_HEI * Settings.RECT_WID);
	}
}
