package com.app.algorithms;

import com.app.Settings;
import com.app.data.Piece;
import com.app.data.QueuePiece;
import com.app.ui.DrawGrid;

import java.util.ArrayList;
import java.util.Stack;
import java.util.function.Supplier;

public class DepthFirst implements ISearchAlgorithm {
    @Override
    public SearchAlgorithm currentAlgorithm() {
        return SearchAlgorithm.DepthFirst;
    }

    @Override
    public void start(Piece startPiece, Piece endPiece, ArrayList<ArrayList<Piece>> grid, DrawGrid gridObj, Supplier<SearchAlgorithm> currentAlgorithm) {
        Stack<QueuePiece> stack = new Stack<>();
        QueuePiece start = new QueuePiece(startPiece.getX(), startPiece.getY());
        start.setType(Piece.Type.Start);

        stack.add(start);

        while (!stack.isEmpty()) {
            var dequeuedQueuePiece = stack.pop();
            assert dequeuedQueuePiece != null;
            var dequeuedPiece = grid.get(dequeuedQueuePiece.getX()).get(dequeuedQueuePiece.getY());
            if (dequeuedPiece.getType() == Piece.Type.Checked) {
                continue;
            }

            if (dequeuedPiece.getType() != Piece.Type.Start) {
                dequeuedPiece.setType(Piece.Type.Checked);
                gridObj.paintImmediately(dequeuedQueuePiece.getX() * gridObj.getRectWid(), dequeuedQueuePiece.getY() * gridObj.getRectHei(), gridObj.getRectWid(), gridObj.getRectHei());

                try {
                    //noinspection BusyWait
                    Thread.sleep(Settings.VISUALIZE_SPEED);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            for (int i = 0; i < 4; i++) {
                if (currentAlgorithm.get() != currentAlgorithm()) {
                    return;
                }

                var checkedPiece = SearchAlgorithmHelper.getPieceByIndex(grid, dequeuedQueuePiece, i);
                if (checkedPiece == null) {
                    continue;
                }

                if (checkedPiece.getType() == Piece.Type.Empty) {
                    QueuePiece checkedQueuePiece = new QueuePiece(checkedPiece);
                    checkedQueuePiece.addParent(dequeuedQueuePiece, checkedQueuePiece);
                    stack.add(checkedQueuePiece);
                } else if (checkedPiece.getType() == Piece.Type.End) {
                    gridObj.drawShortestPath(new ArrayList<>(dequeuedQueuePiece.getPath()));
                    return;
                }
            }
        }

    }
}
