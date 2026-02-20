package com.app.mazes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import com.app.Settings;
import com.app.data.Piece;
import com.app.ui.IDrawGrid;

public class RandomMaze implements IMaze {
	@Override
	public Maze currentMaze() {
		return Maze.Random;
	}

	@Override
	public void generateMaze(List<List<Piece>> grid, IDrawGrid gridObj, Supplier<Maze> currentMaze) {
		// Doing it this way is more pretty
		var piecesGenerated = new ArrayList<Piece>();

		for (List<Piece> colPieceArr : grid) {
			for (Piece curPiece : colPieceArr) {
				double WALL_PERCENTAGE_UNORM = 0.275d;
				if (WALL_PERCENTAGE_UNORM < Math.random()) {
					continue;
				}

				if (MazeUtils.isEmptyPiece(curPiece)) {
					piecesGenerated.add(curPiece);
				}
			}
		}

		Collections.shuffle(piecesGenerated);
		for (var curPiece : piecesGenerated) {
			if (currentMaze.get() != currentMaze()) {
				return;
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
	}
}
