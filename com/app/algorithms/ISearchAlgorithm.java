package com.app.algorithms;

import com.app.ui.DrawGrid;
import com.app.data.Piece;

import java.util.ArrayList;
import java.util.function.Supplier;

public interface ISearchAlgorithm {
    SearchAlgorithm currentAlgorithm();

    void start(Piece startPiece, Piece endPiece, ArrayList<ArrayList<Piece>> grid, DrawGrid gridObj, int visualizeSpeed, Supplier<SearchAlgorithm> currentAlgorithm);
}