package com.app.algorithms;

import com.app.ui.DrawGrid;
import com.app.data.Piece;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public interface ISearchAlgorithm {
    SearchAlgorithm currentAlgorithm();

    CompletableFuture<Void> start(Piece startPiece, Piece endPiece, ArrayList<ArrayList<Piece>> grid, DrawGrid gridObj, int visualizeSpeed);
}