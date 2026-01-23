package com.app.algorithms;

import com.app.ui.DrawGrid;
import com.app.data.Piece;

import java.util.ArrayList;

public interface ISearchAlgorithm {

    public SearchAlgorithm currentAlgorithm();

    public void start(Piece startPiece, Piece endPiece, ArrayList<ArrayList<Piece>> grid, DrawGrid gridObj, int visualizeSpeed);

}