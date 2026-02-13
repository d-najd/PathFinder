package com.app.ui;

import java.util.List;

import com.app.data.Piece;

public interface IDrawGrid {
	/**
	 * Redraw all with animations
	 */
	public void addPiecesForRepainting(Piece... pieces);

	public void drawShortestPath(List<Piece> path);

	public void clearBoard();

	public void clearPath();

	/**
	 * Redraw all immediately, used for initialising the grid at first
	 */
	public void redrawAllImmediate();
}
