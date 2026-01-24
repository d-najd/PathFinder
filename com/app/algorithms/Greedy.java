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

        Queue<QueuePiece> q = new PriorityQueue<>((o1, o2) -> {
            Integer o1d = o1.getDistance();
            Integer o2d = o2.getDistance();
            return o1d.compareTo(o2d);
        });
        QueuePiece start = new QueuePiece(startPiece.getX(), startPiece.getY(), calDis(startPiece.getX(), startPiece.getY())); //Start piece
        start.addParent(new ArrayList<>(), start);
        q.add(start);//Adding start to the queue since we're already visiting it

        while (!q.isEmpty()) {
            List<QueuePiece> previous_ = Collections.unmodifiableList(q.peek().getPath());

            QueuePiece curr = q.poll();
            assert curr != null;
            int curX = curr.getX();
            int curY = curr.getY();

            for (int i = 0; i < 4; i++) { //for each direction
                if ((curX + dx[i] >= 0 && curX + dx[i] < grid.size()) &&
                        (curY + dy[i] >= 0 && curY + dy[i] < grid.getFirst().size())) {
                    //Checked if x and y are correct. ALL IN 1 GO
                    int xc = curX + dx[i];//Setting current x coordinate
                    int yc = curY + dy[i];//Setting current y coordinate
                    var type = grid.get(xc).get(yc).getType();//type of current field

                    if (type == Piece.Type.Empty)//add the piece to the list for it to be processed later
                    {
                        QueuePiece temp = new QueuePiece(xc, yc, calDis(xc, yc));
                        temp.addParent(new ArrayList<>(previous_), temp);

                        //prevents adding the same piece to the list thus preventing a memory leak and LOTS of unnecessary calculations
                        if (q.stream().noneMatch(o -> o.getX() == xc && o.getY() == yc))
                            q.add(temp);//Adding current coordinates to the queue

                    } else if (type == Piece.Type.End) { //Destination found
                        gridObj.drawShortestPath(new ArrayList<>(curr.getPath()));
                        return;
                    }
                }
            }

            //move to the closest piece
            QueuePiece cPiece = q.peek(); //closest piece to the end
            assert cPiece != null;
            int cPieceX = cPiece.getX();
            int cPieceY = cPiece.getY();
            grid.get(cPieceX).get(cPieceY).setType(Piece.Type.Checked);//now BLOCKED

            //paint the piece
            gridObj.piecesForRepainting.add(grid.get(cPieceX).get(cPieceY));
            gridObj.repaint(cPieceX * gridObj.getRectWid(),
                    cPieceY * gridObj.getRectHei(), gridObj.getRectWid(),
                    gridObj.getRectHei());

        }

        System.out.println("no route possible");
    }

    private static int calDis(int x, int y) { //calculate distance from current piece to end piece
        return Math.abs(x - endX) + Math.abs(y - endY);
    }
}


