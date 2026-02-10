package com.app.algorithms;

import java.util.List;
import java.util.function.Supplier;

import com.app.data.Piece;
import com.app.ui.DrawGrid;

public interface ISearchAlgorithm {
	SearchAlgorithm currentAlgorithm();

	void start(Piece startPiece, Piece endPiece, List<List<Piece>> grid, DrawGrid gridObj,
			Supplier<SearchAlgorithm> currentAlgorithm);
}
