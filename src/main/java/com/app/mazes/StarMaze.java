package com.app.mazes;

import java.util.List;

import com.app.data.Piece;
import com.app.ui.DrawGrid;

public class StarMaze implements IMaze {
	@Override
	public Maze currentMaze() {
		return Maze.Star;
	}

	@Override
	public void generateMaze(List<List<Piece>> grid, DrawGrid gridObj) {
	}

	private void fillBorders(List<List<Piece>> grid, DrawGrid gridObj) {

	}
	/*
	 * for (ArrayList<Piece> colPieceArr : grid) {
	 * for (Piece curPiece : colPieceArr) {
	 * assert curPiece.getType() == Piece.Type.Empty || curPiece.getType() ==
	 * Piece.Type.End
	 * || curPiece.getType() == Piece.Type.Start;
	 * if (curPiece.getType() == Piece.Type.Start || curPiece.getType() ==
	 * Piece.Type.End) {
	 * continue;
	 * }
	 * 
	 * double WALL_PERCENTAGE_UNORM = 0.3d;
	 * if (WALL_PERCENTAGE_UNORM < Math.random()) {
	 * continue;
	 * }
	 * 
	 * curPiece.setType(Piece.Type.Wall);
	 * }
	 * gridObj.piecesForRepainting.addAll(colPieceArr);
	 * }
	 * 
	 * gridObj.paintImmediately(0, 0, Settings.GRID_WID * Settings.RECT_WID,
	 * Settings.GRID_HEI * Settings.RECT_WID);
	 * }
	 */
}
