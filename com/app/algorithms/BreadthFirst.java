package com.app.algorithms;

import com.app.Settings;
import com.app.data.Piece;
import com.app.data.QueuePiece;
import com.app.ui.DrawGrid;

import javax.swing.*;
import java.util.*;
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

    static int[] dx = {1, -1, 0, 0};//right, left, NA, NA
    static int[] dy = {0, 0, 1, -1};//NA, NA, bottom, top

    @Override
    public SearchAlgorithm currentAlgorithm() {
        return SearchAlgorithm.BreadthFirst;
    }

    public void start(Piece startPiece, Piece endPiece, ArrayList<ArrayList<Piece>> grid, DrawGrid gridObj, Supplier<SearchAlgorithm> currentAlgorithm) {
        Queue<QueuePiece> queue = new LinkedList<>();
        QueuePiece start = new QueuePiece(startPiece.getX(), startPiece.getY());
        start.addParent(start, start);

        queue.add(start);

        while (queue.peek() != null) {
            var dequeuedPiece = queue.poll();
            assert dequeuedPiece != null;
            for (int i = 0; i < 4; i++)
            {
                if (currentAlgorithm.get() != currentAlgorithm()) {
                    return;
                }

                if ((dequeuedPiece.getX() + dx[i] >= 0 && dequeuedPiece.getX() + dx[i] < grid.size()) &&
                        (dequeuedPiece.getY() + dy[i] >= 0 && dequeuedPiece.getY() + dy[i] < grid.getFirst().size())) {
                    int xc = dequeuedPiece.getX() + dx[i];
                    int yc = dequeuedPiece.getY() + dy[i];
                    var type = grid.get(xc).get(yc).getType();

                    if (type == Piece.Type.Empty)
                    {
                        grid.get(xc).get(yc).setType(Piece.Type.Checked);
                        QueuePiece checkedAgainstPiece = new QueuePiece(xc, yc);
                        checkedAgainstPiece.addParent(dequeuedPiece, checkedAgainstPiece);
                        queue.add(checkedAgainstPiece);


                        gridObj.paintImmediately(checkedAgainstPiece.getX() * gridObj.getRectWid(), checkedAgainstPiece.getY() * gridObj.getRectHei(), gridObj.getRectWid(), gridObj.getRectHei());
                        try {
                            //noinspection BusyWait
                            Thread.sleep(Settings.VISUALIZE_SPEED);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (type == Piece.Type.End) {
                        gridObj.drawShortestPath(new ArrayList<>(dequeuedPiece.getPath()));
                        return;
                    }
                }
            }
        }
        System.out.println("no route possible");
    }
}
