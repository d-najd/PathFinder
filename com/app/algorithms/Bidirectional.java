package com.app.algorithms;

import com.app.data.Piece;
import com.app.data.QueuePiece;
import com.app.ui.DrawGrid;

import java.util.*;
import java.util.function.Supplier;

public class Bidirectional implements ISearchAlgorithm {
    static int[] dx = {1, -1, 0, 0};//right, left, NA, NA
    static int[] dy = {0, 0, 1, -1};//NA, NA, bottom, top

    @Override
    public SearchAlgorithm currentAlgorithm() {
        return SearchAlgorithm.Bidirectional;
    }

    public void start(Piece startPiece, Piece endPiece, ArrayList<ArrayList<Piece>> grid, DrawGrid gridObj, int visualizeSpeed, Supplier<SearchAlgorithm> currentAlgorithm) {
        Queue<QueuePiece> q = new LinkedList<>();
        QueuePiece start = new QueuePiece(startPiece.getX(), startPiece.getY(), Piece.Type.Start);
        QueuePiece end = new QueuePiece(endPiece.getX(), endPiece.getY(), Piece.Type.End);
        start.addParent(start, start);
        end.addParent(end, end);

        q.add(end);
        q.add(start);

        while (q.peek() != null) {
            QueuePiece dequeuedPiece = q.poll();
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
                    Piece checkedAgainstPiece = grid.get(xc).get(yc);

                    if (type == Piece.Type.Empty) {
                        checkedAgainstPiece.setStartType(dequeuedPiece.getStartType());
                        checkedAgainstPiece.setType(Piece.Type.Checked);
                        QueuePiece checkedAgainstQueuePiece = new QueuePiece(xc, yc);
                        checkedAgainstQueuePiece.addParent(dequeuedPiece, checkedAgainstQueuePiece);
                        checkedAgainstQueuePiece.setStartType(dequeuedPiece.getStartType());
                        q.add(checkedAgainstQueuePiece);

                        gridObj.piecesForRepainting.add(checkedAgainstPiece);
                        gridObj.paintImmediately(checkedAgainstQueuePiece.getX() * gridObj.getRectWid(),
                                checkedAgainstQueuePiece.getY() * gridObj.getRectHei(), gridObj.getRectWid(),
                                gridObj.getRectHei());
                        try {
                            //noinspection BusyWait
                            Thread.sleep(visualizeSpeed);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (type == Piece.Type.Checked) {
                        var checkedWith = q.stream().filter(o -> (o.getX() == xc) && (o.getY() == yc)).findAny().orElse(null);

                        if (checkedWith == null) {
                            continue;
                        }

                        if ((dequeuedPiece.getStartType() == Piece.Type.Start && checkedWith.getStartType() == Piece.Type.End) ||
                                (dequeuedPiece.getStartType() == Piece.Type.End && checkedWith.getStartType() == Piece.Type.Start)) {
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
            }
        }
        System.out.println("no route possible");
    }
}