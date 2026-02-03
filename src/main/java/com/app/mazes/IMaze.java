package com.app.mazes;

import com.app.data.Piece;
import com.app.ui.DrawGrid;

import java.util.ArrayList;

public interface IMaze {
	Maze currentMaze();

	void generateMaze(ArrayList<ArrayList<Piece>> grid, DrawGrid gridObj);
}
