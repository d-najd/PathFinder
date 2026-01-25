package com.app.algorithms;

import com.app.Settings;
import com.app.data.Piece;
import com.app.data.QueuePiece;
import com.app.ui.DrawGrid;

import java.util.*;
import java.util.function.Supplier;

public class Greedy implements ISearchAlgorithm {
    /**
     * so the way that this algorithm work is:
     * the algorithm looks at all the neighbors, and it calculates the distance from itself to the finish, it does this
     * by taking his x and y pos and subtracting it from the end's x and y pos, for example if the start is located at
     * 5,10 and the end is located at 2,15 it's going do the following equation ((5-2)+(10-15))=3+5=8 distance away from
     * the end, NOTE it shouldn't support negative numbers so any negative number like in the case with 10-15 will be
     * converted to positive in this case 5, and then the algorithm does that for each of the neighbors and takes the one
     * which is the closest to the end (NOTE it stores the data of the previous neighbors) after it moves it checks if it
     * has any new neighbors calculates how far away they are from the end and takes the next closest and so on till it
     * reaches the end
     */

    @Override
    public SearchAlgorithm currentAlgorithm() {
        return SearchAlgorithm.Greedy;
    }

    public void start(Piece startPiece, Piece endPiece, ArrayList<ArrayList<Piece>> grid, DrawGrid gridObj, Supplier<SearchAlgorithm> currentAlgorithm) {
        endX = endPiece.getX();
        endY = endPiece.getY();

        Queue<QueuePiece> queue = new PriorityQueue<>(Comparator.comparing(this::calculateDistanceFromEnd));
        QueuePiece start = new QueuePiece(startPiece.getX(), startPiece.getY());
        queue.add(start);

        while (!queue.isEmpty()) {
            QueuePiece dequeuedPiece = queue.poll();
            assert dequeuedPiece != null;

            for (int i = 0; i < 4; i++) {
                if (currentAlgorithm.get() != currentAlgorithm()) {
                    return;
                }

                var checkedPiece = SearchAlgorithmHelper.getPieceByIndex(grid, dequeuedPiece, i);
                if (checkedPiece != null) {
                    if (checkedPiece.getType() == Piece.Type.Empty)
                    {
                        QueuePiece checkedQueuePiece = new QueuePiece(checkedPiece);
                        checkedQueuePiece.addParent(dequeuedPiece, checkedQueuePiece);

                        //prevents adding the same piece to the list thus preventing a memory leak and LOTS of unnecessary calculations
                        if (queue.stream().noneMatch(o -> o.getX() == checkedPiece.getX() && o.getY() == checkedPiece.getY()))
                            queue.add(checkedQueuePiece);

                    } else if (checkedPiece.getType() == Piece.Type.End) {
                        gridObj.drawShortestPath(new ArrayList<>(dequeuedPiece.getPath()));
                        return;
                    }
                }
            }

            var checkedQueuePiece = queue.peek();
            assert checkedQueuePiece != null;
            var checkedPiece = grid.get(checkedQueuePiece.getX()).get(checkedQueuePiece.getY());
            checkedPiece.setType(Piece.Type.Checked);

            gridObj.paintImmediately(checkedQueuePiece.getX() * gridObj.getRectWid(),
                    checkedQueuePiece.getY() * gridObj.getRectHei(), gridObj.getRectWid(),
                    gridObj.getRectHei());

            try {
                //noinspection BusyWait
                Thread.sleep(Settings.VISUALIZE_SPEED);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("no route possible");
    }

    private int endX;
    private int endY;

    private Integer calculateDistanceFromEnd(QueuePiece piece) {
        return Math.abs(piece.getX() - endX) + Math.abs(piece.getY() - endY);
    }
}