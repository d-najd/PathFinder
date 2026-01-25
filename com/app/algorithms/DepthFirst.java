package com.app.algorithms;

import com.app.Settings;
import com.app.data.Piece;
import com.app.data.QueuePiece;
import com.app.ui.DrawGrid;

import java.util.ArrayList;
import java.util.Stack;
import java.util.function.Supplier;

public class DepthFirst implements ISearchAlgorithm {
    static int[] dx = {1, -1, 0, 0};//right, left, NA, NA
    static int[] dy = {0, 0, 1, -1};//NA, NA, bottom, top

    @Override
    public SearchAlgorithm currentAlgorithm() {
        return SearchAlgorithm.DepthFirst;
    }

    @Override
    public void start(Piece startPiece, Piece endPiece, ArrayList<ArrayList<Piece>> grid, DrawGrid gridObj, Supplier<SearchAlgorithm> currentAlgorithm) {
        Stack<QueuePiece> stack = new Stack<>();
        QueuePiece start = new QueuePiece(startPiece.getX(), startPiece.getY());

        stack.add(start);

        while (stack.peek() != null) {
            var dequeuedPiece = stack.pop();
            assert dequeuedPiece != null;
            for (int i = 0; i < 4; i++) {
                if (currentAlgorithm.get() != currentAlgorithm()) {
                    return;
                }

                var xc = dequeuedPiece.getX() + dx[i];
                var yc = dequeuedPiece.getY() + dy[i];

                if (xc < 0 || xc >= grid.size() || yc < 0 || yc >= grid.getFirst().size()) {
                    continue;
                }

                var checkedPiece = grid.get(xc).get(yc);

                if (checkedPiece.getType() == Piece.Type.Empty) {
                    checkedPiece.setType(Piece.Type.Checked);
                    QueuePiece checkedQueuePiece = new QueuePiece(xc, yc);
                    checkedQueuePiece.addParent(dequeuedPiece, checkedQueuePiece);
                    stack.add(checkedQueuePiece);

                    gridObj.paintImmediately(checkedQueuePiece.getX() * gridObj.getRectWid(), checkedQueuePiece.getY() * gridObj.getRectHei(), gridObj.getRectWid(), gridObj.getRectHei());
                    try {
                        //noinspection BusyWait
                        Thread.sleep(Settings.VISUALIZE_SPEED);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                } else if (checkedPiece.getType() == Piece.Type.End) {
                    gridObj.drawShortestPath(new ArrayList<>(dequeuedPiece.getPath()));
                    return;
                }
            }
        }

    }
}
