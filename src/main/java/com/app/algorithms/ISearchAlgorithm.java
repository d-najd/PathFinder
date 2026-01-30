package algorithms;

import ui.DrawGrid;
import data.Piece;

import java.util.ArrayList;
import java.util.function.Supplier;

public interface ISearchAlgorithm {
    SearchAlgorithm currentAlgorithm();

    void start(Piece startPiece, Piece endPiece, ArrayList<ArrayList<Piece>> grid, DrawGrid gridObj, Supplier<SearchAlgorithm> currentAlgorithm);
}
