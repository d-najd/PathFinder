package com.app.mazes;

import java.util.List;

import com.app.data.Piece;
import com.app.ui.DrawGrid;

public class RandomMaze implements IMaze {
	@Override
	public Maze currentMaze() {
		return Maze.Random;
	}

	@Override
	public void generateMaze(List<List<Piece>> grid, DrawGrid gridObj) {
		for (List<Piece> colPieceArr : grid) {
			for (Piece curPiece : colPieceArr) {
				double WALL_PERCENTAGE_UNORM = 0.275d;
				if (WALL_PERCENTAGE_UNORM < Math.random()) {
					continue;
				}

				MazeUtils.setPieceToWallIfEmpty(curPiece);
			}
		}

		MazeUtils.repaintAll(grid, gridObj);
	}
}
