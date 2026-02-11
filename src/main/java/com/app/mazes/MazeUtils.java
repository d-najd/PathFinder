package com.app.mazes;

import java.util.List;

import com.app.Settings;
import com.app.data.Piece;
import com.app.ui.DrawGrid;

class MazeUtils {
	public static boolean isEmptyPiece(Piece piece) {
		assert piece.getType() == Piece.Type.Empty || piece.getType() == Piece.Type.End
				|| piece.getType() == Piece.Type.Start;

		if (piece.getType() == Piece.Type.Start || piece.getType() == Piece.Type.End) {
			return false;
		}
		return true;
	}

	public static boolean setPieceToWallIfEmpty(Piece piece) {
		if (isEmptyPiece(piece)) {
			piece.setType(Piece.Type.Wall);
			return true;
		}
		return false;
	}

	public static void repaintAll(List<List<Piece>> grid, DrawGrid gridObj) {
		for (List<Piece> colPieceArr : grid) {
			gridObj.piecesForRepainting.addAll(colPieceArr);
		}

		gridObj.paintImmediately(0, 0, Settings.GRID_WID * Settings.RECT_WID, Settings.GRID_HEI * Settings.RECT_WID);
	}
}
