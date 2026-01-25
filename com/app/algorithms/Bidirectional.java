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

                var checkedPiece = SearchAlgorithmHelper.getPieceByIndex(grid, dequeuedPiece, i);
                if (checkedPiece == null) {
                    continue;
                }

                if (checkedPiece.getType() == Piece.Type.Empty) {
                    checkedPiece.setStartType(dequeuedPiece.getStartType());
                    checkedPiece.setType(Piece.Type.Checked);
                    QueuePiece checkedQueuePiece = new QueuePiece(checkedPiece);
                    checkedQueuePiece.addParent(dequeuedPiece, checkedQueuePiece);
                    checkedQueuePiece.setStartType(dequeuedPiece.getStartType());
                    queue.add(checkedQueuePiece);

                    gridObj.paintImmediately(checkedQueuePiece.getX() * gridObj.getRectWid(),
                            checkedQueuePiece.getY() * gridObj.getRectHei(), gridObj.getRectWid(),
                            gridObj.getRectHei());
                    try {
                        //noinspection BusyWait
                        Thread.sleep(Settings.VISUALIZE_SPEED);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else if (checkedPiece.getType() == Piece.Type.Checked) {
                    var checkedQueuePiece = queue.stream().filter(o -> (o.getX() == checkedPiece.getX()) && (o.getY() == checkedPiece.getY())).findAny().orElse(null);
                    if (checkedQueuePiece == null) {
                        continue;
                    }

                    if (dequeuedPiece.getStartType() == checkedQueuePiece.getStartType()) {
                        continue;
                    }

                    gridObj.drawShortestPath(new ArrayList<>(dequeuedPiece.getPath()));
                    ArrayList<QueuePiece> path = new ArrayList<>(checkedQueuePiece.getPath());
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