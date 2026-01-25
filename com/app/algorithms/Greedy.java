package com.app.algorithms;

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

    static int[] dx = {1, -1, 0, 0};//right, left, NA, NA
    static int[] dy = {0, 0, 1, -1};//NA, NA, bottom, top

    private static int endX;
    private static int endY;

    @Override
    public SearchAlgorithm currentAlgorithm() {
        return SearchAlgorithm.Greedy;
    }

    public void start(Piece startPiece, Piece endPiece, ArrayList<ArrayList<Piece>> grid, DrawGrid gridObj, int visualizeSpeed, Supplier<SearchAlgorithm> currentAlgorithm) {
        endX = endPiece.getX();
        endY = endPiece.getY();

        Queue<QueuePiece> q = new PriorityQueue<>(Comparator.comparing(Greedy::calculateDistanceFromEnd));
        QueuePiece start = new QueuePiece(startPiece.getX(), startPiece.getY());
        start.addParent(start, start);
        q.add(start);

        while (!q.isEmpty()) {
            QueuePiece curr = q.poll();
            assert curr != null;
            int curX = curr.getX();
            int curY = curr.getY();

            for (int i = 0; i < 4; i++) {
                if ((curX + dx[i] >= 0 && curX + dx[i] < grid.size()) &&
                        (curY + dy[i] >= 0 && curY + dy[i] < grid.getFirst().size())) {
                    int xc = curX + dx[i];
                    int yc = curY + dy[i];
                    var type = grid.get(xc).get(yc).getType();

                    if (type == Piece.Type.Empty)
                    {
                        QueuePiece temp = new QueuePiece(xc, yc);
                        temp.addParent(curr, temp);

                        //prevents adding the same piece to the list thus preventing a memory leak and LOTS of unnecessary calculations
                        if (q.stream().noneMatch(o -> o.getX() == xc && o.getY() == yc))
                            q.add(temp);

                    } else if (type == Piece.Type.End) {
                        gridObj.drawShortestPath(new ArrayList<>(curr.getPath()));
                        return;
                    }
                }
            }

            QueuePiece cPiece = q.peek();
            assert cPiece != null;
            int cPieceX = cPiece.getX();
            int cPieceY = cPiece.getY();
            grid.get(cPieceX).get(cPieceY).setType(Piece.Type.Checked);

            gridObj.paintImmediately(cPieceX * gridObj.getRectWid(),
                    cPieceY * gridObj.getRectHei(), gridObj.getRectWid(),
                    gridObj.getRectHei());

            try {
                //noinspection BusyWait
                Thread.sleep(visualizeSpeed);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("no route possible");
    }

    private static Integer calculateDistanceFromEnd(QueuePiece piece) {
        return Math.abs(piece.getX() - endX) + Math.abs(piece.getY() - endY);
    }
}