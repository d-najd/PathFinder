package com.app.mazes;

public class RecursiveDivisionMazeVerticalSkew extends RecursiveDivisionMaze {
	@Override
	public Maze currentMaze() {
		return Maze.RecursiveDivisionVerticalSkew;
	}

	@Override
	protected float horizontalSplitWeight() {
		return 0.25f;
	}

	@Override
	protected SplitType determineSplitType(int width, int height) {
		return determineSplitTypeUnWeighted(width, height);
	}
}
