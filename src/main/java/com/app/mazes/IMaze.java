package com.app.mazes;

import com.app.data.Piece;
import com.app.ui.DrawGrid;

import java.util.List;

public interface IMaze {
	Maze currentMaze();

	void generateMaze(List<List<Piece>> grid, DrawGrid gridObj);
}
