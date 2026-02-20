package com.app.algorithms;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Supplier;

import com.app.Settings;
import com.app.data.Piece;
import com.app.data.QueuePiece;
import com.app.ui.IDrawGrid;

public class BreadthFirst implements ISearchAlgorithm {
	/**
	 * creating an unmodifiable instance of the last list selected, so it doesn't
	 * modify when for example if
	 * there is a grid with this layout, where S is start, E is empty and F is
	 * finish:
	 * <p>
	 * S E
	 * E F
	 * <p>
	 * when the list starts at S it moves to right and E is added to the list so if
	 * we move down we get the list
	 * with S and E, but we should be getting only S instead, so this is what this
	 * code does, only getting S
	 * instead of the all passed elements in the lists and melting the pc
	 */

	@Override
	public SearchAlgorithm currentAlgorithm() {
		return SearchAlgorithm.BreadthFirst;
	}

	public void start(Piece startPiece, Piece endPiece, List<List<Piece>> grid, IDrawGrid gridObj,
			Supplier<SearchAlgorithm> currentAlgorithm) {
		Queue<QueuePiece> queue = new LinkedList<>();
		var start = new QueuePiece(startPiece.getX(), startPiece.getY());

		queue.add(start);

		while (queue.peek() != null) {
			var dequeuedPiece = queue.poll();
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
					checkedPiece.setType(Piece.Type.Checked);
					QueuePiece checkedQueuePiece = new QueuePiece(checkedPiece);
					checkedQueuePiece.addParent(dequeuedPiece, checkedQueuePiece);
					queue.add(checkedQueuePiece);

					gridObj.addPiecesForRepainting(checkedPiece);

					// gridObj.paintImmediately(checkedQueuePiece.getX() * gridObj.getRectWid(),
					// checkedQueuePiece.getY() * gridObj.getRectHei(), gridObj.getRectWid(),
					// gridObj.getRectHei());
					try {
						// noinspection BusyWait
						Thread.sleep(Settings.VISUALIZE_SPEED);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				} else if (checkedPiece.getType() == Piece.Type.End) {
					var path = dequeuedPiece.getPath().stream().map(o -> (Piece) o).toList(); // this is fucking stupid
					gridObj.drawShortestPath(path);
					return;
				}
			}
		}
		System.out.println("no route possible");
	}
}
