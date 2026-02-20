package com.app.mazes;

import java.util.List;
import java.util.function.Supplier;

import com.app.data.Piece;
import com.app.ui.IDrawGrid;

public interface IMaze {
	Maze currentMaze();

	void generateMaze(List<List<Piece>> grid, IDrawGrid gridObj, Supplier<Maze> currentMaze);
}
