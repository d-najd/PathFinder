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
            QueuePiece curr = q.poll();
            assert curr != null;
            int curX = curr.getX();
            int curY = curr.getY();
            for (int i = 0; i < 4; i++) //for each direction
            {
                if ((curX + dx[i] >= 0 && curX + dx[i] < grid.size()) &&
                        (curY + dy[i] >= 0 && curY + dy[i] < grid.getFirst().size())) {
                    int xc = curX + dx[i];
                    int yc = curY + dy[i];
                    var type = grid.get(xc).get(yc).getType();
                    Piece tempPiece = grid.get(xc).get(yc);

                    if (type == Piece.Type.Empty) {
                        tempPiece.setStartType(curr.getStartType());
                        tempPiece.setType(Piece.Type.Checked);
                        QueuePiece temp = new QueuePiece(xc, yc);
                        temp.addParent(curr, temp);
                        temp.setStartType(curr.getStartType());
                        q.add(temp);

                        gridObj.paintImmediately(temp.getX() * gridObj.getRectWid(),
                                temp.getY() * gridObj.getRectHei(), gridObj.getRectWid(),
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

                        if ((curr.getStartType() == Piece.Type.Start && checkedWith.getStartType() == Piece.Type.End) ||
                                (curr.getStartType() == Piece.Type.End && checkedWith.getStartType() == Piece.Type.Start)) {
                            gridObj.drawShortestPath(new ArrayList<>(curr.getPath()));
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