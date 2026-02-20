package com.app.mazes;

import java.util.List;
import java.util.Random;

import com.app.Settings;
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
		var remainingSize = splitType == SplitType.Horizontal ? remainingHeight : remainingWidth;
		var remainingSizeOpposite = splitType == SplitType.Vertical ? remainingHeight : remainingWidth;

		if (remainingSize <= 2) {
			return;
		}

		var splitIndex = new Random().nextInt(remainingSize - 2);
		var pathPieceIndex = new Random().nextInt(remainingSizeOpposite);

		for (int i = 0; i < remainingSizeOpposite; i++) {
			if (i == pathPieceIndex) {
				continue;
			}

			var curPiece = splitType == SplitType.Horizontal ? grid.get(i).get(splitIndex + 1)
					: grid.get(splitIndex + 1).get(i);

			if (!MazeUtils.isEmptyPiece(curPiece)) {
				continue;
			}

			curPiece.setType(Piece.Type.Wall);
			gridObj.addPiecesForRepainting(curPiece);

			try {
				// noinspection BusyWait
				Thread.sleep(Settings.MAZE_GEN_SPEED);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		if (splitType == SplitType.Horizontal) {
			divide(grid.stream().map(o -> o.subList(0, splitIndex)).toList(), gridObj);
			divide(grid.stream().map(o -> o.subList(splitIndex + 2, Math.max(splitIndex +
					2, remainingHeight))).toList(),
					gridObj);
		} else {
			divide(grid.subList(0, splitIndex), gridObj);
			divide(grid.subList(splitIndex + 2, Math.max(splitIndex + 2,
					remainingWidth)), gridObj);
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
