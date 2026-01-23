package com.app.Algorithms;

import com.app.ui.DrawGrid;
import com.app.Objects.Piece;

import java.util.ArrayList;

public interface ISearchAlgorithm {

    public SearchAlgorithm currentAlgorithm();

    public void start(Piece startPiece, Piece endPiece, ArrayList<ArrayList<Piece>> grid, DrawGrid gridObj, int visualizeSpeed);

}