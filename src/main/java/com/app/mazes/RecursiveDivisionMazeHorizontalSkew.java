package com.app.mazes;

public class RecursiveDivisionMazeHorizontalSkew extends RecursiveDivisionMaze {
	@Override
	public Maze currentMaze() {
		return Maze.RecursiveDivisionHorizontalSkew;
	}

	@Override
	protected float horizontalSplitWeight() {
		return 0.75f;
	}

	@Override
	protected SplitType determineSplitType(int width, int height) {
		return determineSplitTypeUnWeighted(width, height);
	}
}
