package com.app.algorithms;

import com.app.Settings;
import com.app.data.Piece;
import com.app.data.QueuePiece;
import com.app.ui.DrawGrid;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

public class BreadthFirst implements ISearchAlgorithm {
    /**
     * creating an unmodifiable instance of the last list selected, so it doesn't modify when for example if
     * there is a grid with this layout, where S is start, E is empty and F is finish:
     * <p>
     * S E
     * E F
     * <p>
     * when the list starts at S it moves to right and E is added to the list so if we move down we get the list
     * with S and E, but we should be getting only S instead, so this is what this code does, only getting S
     * instead of the all passed elements in the lists and melting the pc
     */


    @Override
    public SearchAlgorithm currentAlgorithm() {
        return SearchAlgorithm.BreadthFirst;
    }

    public void start(Piece startPiece, Piece endPiece, ArrayList<ArrayList<Piece>> grid, DrawGrid gridObj, Supplier<SearchAlgorithm> currentAlgorithm) {
        Queue<QueuePiece> queue = new LinkedList<>();
        var start = new QueuePiece(startPiece.getX(), startPiece.getY());

        queue.add(start);

        while (queue.peek() != null) {
            var dequeuedPiece = queue.poll();
            assert dequeuedPiece != null;

            for (int i = 0; i < 4; i++) {
                if (currentAlgorithm.get() != currentAlgorithm()) {
                    return;
                }

                var xc = dequeuedPiece.getX() + SearchAlgorithmHelper.dx[i];
                var yc = dequeuedPiece.getY() + SearchAlgorithmHelper.dy[i];

                if (xc < 0 || xc >= grid.size() || yc < 0 || yc >= grid.getFirst().size()) {
                    continue;
                }

                var checkedAgainstPiece = grid.get(xc).get(yc);
                if (checkedAgainstPiece.getType() == Piece.Type.Empty) {
                    checkedAgainstPiece.setType(Piece.Type.Checked);
                    QueuePiece checkedAgainstQueuePiece = new QueuePiece(xc, yc);
                    checkedAgainstQueuePiece.addParent(dequeuedPiece, checkedAgainstQueuePiece);
                    queue.add(checkedAgainstQueuePiece);

                    gridObj.paintImmediately(checkedAgainstQueuePiece.getX() * gridObj.getRectWid(), checkedAgainstQueuePiece.getY() * gridObj.getRectHei(), gridObj.getRectWid(), gridObj.getRectHei());
                    try {
                        //noinspection BusyWait
                        Thread.sleep(Settings.VISUALIZE_SPEED);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else if (checkedAgainstPiece.getType() == Piece.Type.End) {
                    gridObj.drawShortestPath(dequeuedPiece.getPath());
                    return;
                }
            }
        }
        System.out.println("no route possible");
    }
}
