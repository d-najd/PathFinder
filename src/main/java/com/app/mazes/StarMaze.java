package com.app.mazes;

import java.util.List;
import java.util.function.Supplier;

import com.app.data.Piece;
import com.app.ui.IDrawGrid;

public class StarMaze implements IMaze {
	@Override
	public Maze currentMaze() {
		return Maze.Star;
	}

	@Override
	public void generateMaze(List<List<Piece>> grid, IDrawGrid gridObj, Supplier<Maze> currentMaze) {
		// MazeUtils.repaintAll(grid, gridObj);
	}

}
