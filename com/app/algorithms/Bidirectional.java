package com.app.algorithms;

import com.app.Settings;
import com.app.data.Piece;
import com.app.data.QueuePiece;
import com.app.ui.DrawGrid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

public class Bidirectional implements ISearchAlgorithm {
    static int[] dx = {1, -1, 0, 0};//right, left, NA, NA
    static int[] dy = {0, 0, 1, -1};//NA, NA, bottom, top

    @Override
    public SearchAlgorithm currentAlgorithm() {
        return SearchAlgorithm.Bidirectional;
    }

    public void start(Piece startPiece, Piece endPiece, ArrayList<ArrayList<Piece>> grid, DrawGrid gridObj, Supplier<SearchAlgorithm> currentAlgorithm) {
        Queue<QueuePiece> queue = new LinkedList<>();
        QueuePiece start = new QueuePiece(startPiece.getX(), startPiece.getY(), Piece.Type.Start);
        QueuePiece end = new QueuePiece(endPiece.getX(), endPiece.getY(), Piece.Type.End);

        queue.add(end);
        queue.add(start);

        while (queue.peek() != null) {
            QueuePiece dequeuedPiece = queue.poll();
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

                Piece checkedAgainstPiece = grid.get(xc).get(yc);

                if (checkedAgainstPiece.getType() == Piece.Type.Empty) {
                    checkedAgainstPiece.setStartType(dequeuedPiece.getStartType());
                    checkedAgainstPiece.setType(Piece.Type.Checked);
                    QueuePiece checkedAgainstQueuePiece = new QueuePiece(xc, yc);
                    checkedAgainstQueuePiece.addParent(dequeuedPiece, checkedAgainstQueuePiece);
                    checkedAgainstQueuePiece.setStartType(dequeuedPiece.getStartType());
                    queue.add(checkedAgainstQueuePiece);

                    gridObj.paintImmediately(checkedAgainstQueuePiece.getX() * gridObj.getRectWid(),
                            checkedAgainstQueuePiece.getY() * gridObj.getRectHei(), gridObj.getRectWid(),
                            gridObj.getRectHei());
                    try {
                        //noinspection BusyWait
                        Thread.sleep(Settings.VISUALIZE_SPEED);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else if (checkedAgainstPiece.getType() == Piece.Type.Checked) {
                    var checkedWith = queue.stream().filter(o -> (o.getX() == xc) && (o.getY() == yc)).findAny().orElse(null);

                    if (checkedWith == null) {
                        continue;
                    }

                    if (dequeuedPiece.getStartType() == checkedWith.getStartType()) {
                        continue;
                    }

                    gridObj.drawShortestPath(new ArrayList<>(dequeuedPiece.getPath()));
                    ArrayList<QueuePiece> path = new ArrayList<>(checkedWith.getPath());
                    Collections.reverse(path);
                    path.addFirst(new QueuePiece(-1, -1));
                    path.removeLast();
                    gridObj.drawShortestPath(path);
                    return;
                }
            }
        }
        System.out.println("no route possible");
    }
}