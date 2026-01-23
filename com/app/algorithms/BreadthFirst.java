package com.app.algorithms;

import com.app.data.Piece;
import com.app.data.QueuePiece;
import com.app.ui.DrawGrid;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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

    static int[] dx = {1, -1, 0, 0};//right, left, NA, NA
    static int[] dy = {0, 0, 1, -1};//NA, NA, bottom, top

    @Override
    public SearchAlgorithm currentAlgorithm() {
        return SearchAlgorithm.BreadthFirst;
    }

    public void start(Piece startPiece, Piece endPiece, ArrayList<ArrayList<Piece>> grid, DrawGrid gridObj, int visualizeSpeed, Supplier<SearchAlgorithm> currentAlgorithm) {
        Queue<QueuePiece> q = new LinkedList<>();
        QueuePiece start = new QueuePiece(startPiece.getX(), startPiece.getY()); //Start piece
        start.addParent(new ArrayList<>(), start);

        q.add(start);//Adding start to the queue since we're already visiting it

        while (q.peek() != null) {
            List<QueuePiece> previous = List.copyOf(q.peek().getPath()); //immutable and NO YOU CAN'T MAKE THIS MUTABLE IT WILL BREAK ANYTHING

            QueuePiece curr = q.poll();//poll or remove. Same thing
            assert curr != null;
            int curX = curr.getX();
            int curY = curr.getY();
            for (int i = 0; i < 4; i++)//for each direction
            {
                if ((curX + dx[i] >= 0 && curX + dx[i] < grid.size()) &&
                        (curY + dy[i] >= 0 && curY + dy[i] < grid.getFirst().size())) {
                    //Checked if x and y are correct. ALL IN 1 GO
                    int xc = curX + dx[i];//Setting current x coordinate
                    int yc = curY + dy[i];//Setting current y coordinate
                    var type = grid.get(xc).get(yc).getType();//type of current field

                    if (type == Piece.Type.Empty)//Movable. Can't return here again so setting it to 'Blocked' now
                    {
                        grid.get(xc).get(yc).setType(Piece.Type.Checked);//now BLOCKED
                        QueuePiece temp = new QueuePiece(xc, yc);
                        temp.addParent(new ArrayList<>(previous), temp);
                        q.add(temp);//Adding current coordinates to the queue

                        //paint the piece

                        if (currentAlgorithm.get() != currentAlgorithm()) {
                            return;
                        }

                        gridObj.paintImmediately(temp.getX() * gridObj.getRectWid(),
                                temp.getY() * gridObj.getRectHei(), gridObj.getRectWid(),
                                gridObj.getRectHei());

                        try {
                            //noinspection BusyWait
                            Thread.sleep(visualizeSpeed);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (type == Piece.Type.End) { //Destination found
                        gridObj.drawShortestPath(new ArrayList<>(curr.getPath()));
                        return;
                    }
                }
            }
        }
        System.out.println("no route possible");
    }
}
