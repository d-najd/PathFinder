package com.app.mazes;

import com.app.data.Piece;

class MazeUtils {
	public static boolean isEmptyPiece(Piece curPiece) {
		assert curPiece.getType() == Piece.Type.Empty || curPiece.getType() == Piece.Type.End
				|| curPiece.getType() == Piece.Type.Start;

		if (curPiece.getType() == Piece.Type.Start || curPiece.getType() == Piece.Type.End) {
			return false;
		}
		return true;
	}
}
