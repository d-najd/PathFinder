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

        stack.add(start);

        while (!stack.isEmpty()) {
            var dequeuedPiece = stack.pop();
            assert dequeuedPiece != null;

            boolean foundEmptyPiece = false;
            for (int i = 0; i < 4; i++) {
                if (currentAlgorithm.get() != currentAlgorithm()) {
                    return;
                }

                var xc = dequeuedPiece.getX() + SearchAlgorithmHelper.dx[i];
                var yc = dequeuedPiece.getY() + SearchAlgorithmHelper.dy[i];

                if (xc < 0 || xc >= grid.size() || yc < 0 || yc >= grid.getFirst().size()) {
                    continue;
                }

                var checkedPiece = grid.get(xc).get(yc);

                if (checkedPiece.getType() == Piece.Type.Empty) {
                    QueuePiece checkedQueuePiece = new QueuePiece(xc, yc);
                    checkedQueuePiece.addParent(dequeuedPiece, checkedQueuePiece);
                    stack.add(checkedQueuePiece);
                    if (!foundEmptyPiece) {
                        checkedPiece.setType(Piece.Type.Checked);
                        gridObj.paintImmediately(checkedQueuePiece.getX() * gridObj.getRectWid(), checkedQueuePiece.getY() * gridObj.getRectHei(), gridObj.getRectWid(), gridObj.getRectHei());
                        try {
                            //noinspection BusyWait
                            Thread.sleep(Settings.VISUALIZE_SPEED);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    foundEmptyPiece = true;
                } else if (checkedPiece.getType() == Piece.Type.End) {
                    gridObj.drawShortestPath(new ArrayList<>(dequeuedPiece.getPath()));
                    return;
                }
            }
        }

    }
}
