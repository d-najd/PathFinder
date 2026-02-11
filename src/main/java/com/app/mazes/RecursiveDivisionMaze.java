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
		divide(grid, gridObj);
		MazeUtils.repaintAll(grid, gridObj);
	}

	private static void divide(List<List<Piece>> grid, DrawGrid gridObj) {
		if (grid.size() == 0) {
			return;
		}

		var remainingWidth = grid.size();
		var remainingHeight = grid.getFirst().size();

		var splitType = determineSplitType(remainingWidth, remainingHeight);

		if (splitType == SplitType.Horizontal) {
			if (remainingHeight <= 2) {
				return;
			}

			var splitIndex = new Random().nextInt(remainingHeight - 2);
			var pathPieceIndex = new Random().nextInt(remainingWidth);

			for (int i = 0; i < remainingWidth; i++) {
				if (i == pathPieceIndex) {
					continue;
				}

				MazeUtils.setPieceToWallIfEmpty(grid.get(i).get(splitIndex + 1));
			}

			divide(grid.stream().map(o -> o.subList(0, splitIndex)).toList(), gridObj);
			divide(grid.stream().map(o -> o.subList(splitIndex + 2, Math.max(splitIndex + 2, remainingHeight))).toList(),
					gridObj);
		} else {
			if (remainingWidth <= 2) {
				return;
			}

			var splitIndex = new Random().nextInt(remainingWidth - 2);
			var pathPieceIndex = new Random().nextInt(remainingHeight);

			for (int i = 0; i < remainingHeight; i++) {
				if (i == pathPieceIndex) {
					continue;
				}

				MazeUtils.setPieceToWallIfEmpty(grid.get(splitIndex + 1).get(i));
			}

			divide(grid.subList(0, splitIndex), gridObj);
			divide(grid.subList(splitIndex + 2, Math.max(splitIndex + 2, remainingWidth)), gridObj);
		}
	}

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

		var widthUNormPercentage = (float) width / (width + height);
		if (new Random().nextFloat() < widthUNormPercentage) {
			return SplitType.Vertical;
		} else {
			return SplitType.Horizontal;
		}
	}

	private enum SplitType {
		Vertical,
		Horizontal
	}
}
