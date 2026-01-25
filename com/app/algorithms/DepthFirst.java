package com.app.algorithms;

import com.app.Settings;
import com.app.data.Piece;
import com.app.data.QueuePiece;
import com.app.ui.DrawGrid;

import java.util.ArrayList;
import java.util.Stack;
import java.util.function.Supplier;

public class DepthFirst implements ISearchAlgorithm {
    static int[] dx = {1, -1, 0, 0};//right, left, NA, NA
    static int[] dy = {0, 0, 1, -1};//NA, NA, bottom, top

    @Override
    public SearchAlgorithm currentAlgorithm() {
        return SearchAlgorithm.DepthFirst;
    }

    @Override
    public void start(Piece startPiece, Piece endPiece, ArrayList<ArrayList<Piece>> grid, DrawGrid gridObj, Supplier<SearchAlgorithm> currentAlgorithm) {
        Stack<QueuePiece> stack = new Stack<>();
        QueuePiece start = new QueuePiece(startPiece.getX(), startPiece.getY());

        stack.add(start);

        while (stack.peek() != null) {
            var dequeuedPiece = stack.pop();
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
                        stack.add(checkedAgainstPiece);

                        gridObj.paintImmediately(checkedAgainstPiece.getX() * gridObj.getRectWid(), checkedAgainstPiece.getY() * gridObj.getRectHei(), gridObj.getRectWid(), gridObj.getRectHei());
                        try {
                            //noinspection BusyWait
                            Thread.sleep(Settings.VISUALIZE_SPEED);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    } else if (type == Piece.Type.End) {
                        gridObj.drawShortestPath(new ArrayList<>(dequeuedPiece.getPath()));
                        return;
                    }
                }
            }
        }

    }
}
