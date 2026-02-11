package com.app.mazes;

import java.util.List;
import java.util.Random;

import com.app.data.Piece;
import com.app.ui.DrawGrid;

public class RecursiveDivisionMaze implements IMaze {
	@Override
	public Maze currentMaze() {
		return Maze.RecursiveDevision;
	}

	@Override
	public void generateMaze(List<List<Piece>> grid, DrawGrid gridObj) {
		// fillBorders(grid, gridObj);

		// VERTICAL
		// grid.getFirst().forEach(o -> o.setType(Piece.Type.Wall));

		/*
		 * for (int i = 0; i < remainingWidth; i++) {
		 * grid.get(i).get(3).setType(Piece.Type.Wall);
		 * }
		 */

		divide(grid, gridObj);

		MazeUtils.repaintAll(grid, gridObj);
	}

	private static void divide(List<List<Piece>> grid, DrawGrid gridObj) {
		var remainingWidth = grid.size();
		var remainingHeight = grid.getFirst().size();

		if (remainingHeight <= 1 || remainingWidth <= 1) {
			return;
		}

		if (remainingWidth <= 2 && remainingHeight <= 2) {
			return;
		}

		// var splitType = determineSplitType(remainingWidth, remainingHeight);
		var splitType = SplitType.Horizontal;

		if (splitType == SplitType.Horizontal) {
			if (remainingHeight <= 2) {
				return;
			}

			var index = new Random().nextInt(remainingHeight - 2); // TODO not sure if it should be -1 here or -2

			for (int i = 0; i < remainingWidth; i++) {
				grid.get(i).get(index + 1).setType(Piece.Type.Wall);
			}

			divide(grid.stream().map(o -> o.subList(0, index)).toList(), gridObj);
			if (grid.size() > index + 2) {
				divide(grid.stream().map(o -> o.subList(index + 2, grid.getFirst().size())).toList(), gridObj);
			}
		} else {

		}
	}

	/**
	 * gets the grid except the outside pieces
	 * Example:
	 * x pieces returned
	 * 0 pieces ignored
	 *
	 * 0000
	 * xxxx
	 * xxxx
	 * 0000
	 */
	private List<List<Piece>> getExceptHorizontalBorder(List<List<Piece>> grid, DrawGrid gridObj) {
		return grid.subList(1, grid.size() - 1);
	}

	/**
	 * gets the grid except the outside pieces
	 * Example:
	 * x pieces returned
	 * 0 pieces ignored
	 *
	 * 0xx0
	 * 0xx0
	 * 0xx0
	 * 0xx0
	 */
	private List<List<Piece>> getExceptVerticalBorder(List<List<Piece>> grid, DrawGrid gridObj) {
		return grid.stream().map(o -> o.subList(1, o.size() - 1)).toList();
	}

	/**
	 * If size is 1 or less (x or y) invalid
	 * If size 2 on both invalid
	 * If size 2 pick the other
	 */

	/**
	 * Determines [SplitType], it is skewed to pick the [SplitType] which has more
	 * remaining, there is more width remaining
	 * [SplitType.Horizontal] is more likely to be picked and vice versa
	 */
	private static SplitType determineSplitType(int width, int height) {
		if (width <= 2) {
			return SplitType.Vertical;
		}

		if (height <= 2) {
			return SplitType.Horizontal;
		}

		var widthUNormPercentage = width / (width + height);
		if (new Random().nextFloat() < widthUNormPercentage) {
			return SplitType.Vertical;
		} else {
			return SplitType.Horizontal;
		}
	}

	private static void fillBorders(List<List<Piece>> grid, DrawGrid gridObj) {
		for (Piece curPiece : grid.getFirst()) {
			MazeUtils.setPieceToWallIfEmpty(curPiece);
		}

		for (List<Piece> curCol : grid.subList(1, grid.size())) {
			MazeUtils.setPieceToWallIfEmpty(curCol.getFirst());
			MazeUtils.setPieceToWallIfEmpty(curCol.getLast());
		}

		for (Piece curPiece : grid.getLast().subList(1, grid.size() - 1)) {
			MazeUtils.setPieceToWallIfEmpty(curPiece);
		}
	}

	private enum SplitType {
		Vertical,
		Horizontal
	}
}
