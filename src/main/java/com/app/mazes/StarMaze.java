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

		MazeUtils.repaintAll(grid, gridObj);
	}

}
