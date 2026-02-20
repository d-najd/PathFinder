package com.app.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.app.Settings;
import com.app.animations.Animator;
import com.app.data.Piece;

public class DrawGrid extends JPanel implements IDrawGrid {
	public List<List<Piece>> gridPieces = new ArrayList<>();
	protected Piece startPiece;
	protected Piece endPiece;

	/**
	 * Draw pieces in [piecesForRepainting] on next [paintComponent] call completing
	 * all animations instantly
	 */
	private boolean redrawSkipAnimations;
	private Piece wasPreviousPieceUnique; // by unique it means start or end position, because when you hold the left
	public List<Piece> piecesForRepainting = new CopyOnWriteArrayList<>();
	private long timeSinceLastRepaint = System.currentTimeMillis();

	private final Timer timer;

	public DrawGrid() {
		setLayout(null);
		setBounds(Settings.GRID_OFFSET_X, Settings.GRID_OFFSET_Y, Settings.GRID_WID * getRectWid() + 1,
				Settings.GRID_HEI * getRectHei() + 1);

		createGridPieces();
		setStartPositions();

		// redrawSkipAnimations = true;
		redrawAllImmediate();

		timer = new Timer(16, e -> {
			this.repaint();
		});

		timer.setRepeats(true);
		timer.start();
	}

	@Override
	public void addPiecesForRepainting(Piece... pieces) {
		piecesForRepainting.addAll(List.of(pieces));
	}

	@Override
	public void redrawAllImmediate() {
		this.piecesForRepainting.clear();
		addPiecesForRepainting(gridPieces.stream().flatMap(List::stream).toArray(Piece[]::new));
		redrawSkipAnimations = true;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		var piecesCopy = piecesForRepainting.stream().toList();
		var curTime = System.currentTimeMillis();

		var otherPieces = gridPieces.stream().flatMap(List::stream).toList();
		for (Piece curpiece : otherPieces) {
			repaintPiece(g2d, curpiece, true);
		}

		if (redrawSkipAnimations) {
			for (Piece curPiece : piecesCopy) {
				piecesForRepainting.clear();
				repaintPiece(g2d, curPiece);
				curPiece.notifyAnimationFinished();
			}
			redrawSkipAnimations = false;
		} else {
			for (Piece curPiece : piecesCopy) {
				var elapsedTime = timeSinceLastRepaint - curTime;
				var newPercentage = curPiece.getAnimationPercentage()
						- ((float) elapsedTime / curPiece.getAnimationLengthMilli());

				if (curPiece.isImmediatelySetType()) {
					System.out.println(curPiece.getX() + "X " + curPiece.getY() + "Y PAINT IMMEDIATE");
					// If piece gets modified to something while it's painting, will immediatelly
					// finish current anim and start with the new type
					piecesForRepainting.remove(curPiece);
					curPiece.setAnimationPercentage(0);
					curPiece.setImmediatelySetType(false);
					repaintPiece(g2d, curPiece);
				} else if (newPercentage > 1.00) {
					// System.out.println(curPiece.getX() + "X " + curPiece.getY() + "Y FINISHED");
					piecesForRepainting.remove(curPiece);
					repaintPiece(g2d, curPiece);
					curPiece.notifyAnimationFinished();
				} else {
					// System.out.println(curPiece.getX() + "X " + curPiece.getY() + "Y " +
					// newPercentage);
					curPiece.setAnimationPercentage(newPercentage);
					var animationRect = Animator.ripple(curPiece);
					g2d.setColor(curPiece.getColor());
					g2d.fill(animationRect);
				}
			}
			// System.out.print(curTime);
		}

		timeSinceLastRepaint = curTime;
	}

	private void repaintPiece(Graphics2D g2d, Piece curPiece) {
		repaintPiece(g2d, curPiece, false);
	}

	private void repaintPiece(Graphics2D g2d, Piece curPiece, boolean usePreviousColor) {
		g2d.setColor(!usePreviousColor ? curPiece.getColor() : curPiece.getPreviousColor());
		g2d.fill(curPiece.getRect());
		g2d.setColor(Color.black);
		g2d.draw(curPiece.getRect());
	}

	public void drawShortestPath(List<Piece> path) {
		var expectedAlgorithm = ContentButtons.getRunningAlgorithm();
		if (expectedAlgorithm == null) {
			return;
		}
		for (int i = 1; i < path.size(); i++) {
			if (expectedAlgorithm != ContentButtons.getRunningAlgorithm()) {
				return;
			}

			var curPiece = path.get(i);
			gridPieces.get(curPiece.getX()).get(curPiece.getY()).setType(Piece.Type.DisplayingPath);// display the shortest
																																	// path type
			addPiecesForRepainting(gridPieces.get(curPiece.getX()).get(curPiece.getY()));
			try {
				// noinspection BusyWait
				Thread.sleep(Settings.SHORTEST_VISUALIZE_SPEED);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void clearBoard() {
		for (List<Piece> colPieceArr : gridPieces) {
			for (Piece curPiece : colPieceArr) {
				if (curPiece.getType() == Piece.Type.Checked || curPiece.getType() == Piece.Type.DisplayingPath
						|| curPiece.getType() == Piece.Type.Wall) {
					curPiece.setType(Piece.Type.Empty);
					addPiecesForRepainting(curPiece);
				}
			}
		}
	}

	public void clearPath() {
		for (List<Piece> colPieceArr : gridPieces) {
			for (Piece curPiece : colPieceArr) {
				if (curPiece.getType() == Piece.Type.Checked || curPiece.getType() == Piece.Type.DisplayingPath) {
					curPiece.setType(Piece.Type.Empty);
					addPiecesForRepainting(curPiece);
				}
			}
		}
	}

	private void setStartPositions() {
		startPiece = gridPieces.getFirst().getFirst();
		startPiece.setType(Piece.Type.Start);
		endPiece = gridPieces.get(Settings.GRID_WID - 1).get(Settings.GRID_HEI - 1);
		endPiece.setType(Piece.Type.End);
	}

	private void createGridPieces() {
		if (!gridPieces.isEmpty())
			System.out.println("[ERROR] there are already pieces when creating the grid wtf?");

		for (int x = 0; x < Settings.GRID_WID; x++) {
			ArrayList<Piece> tempArr = new ArrayList<>();
			for (int y = 0; y < Settings.GRID_HEI; y++) {
				var newPiece = new Piece(x, y);
				tempArr.add(newPiece);
			}
			gridPieces.add(tempArr);
		}
		new GridListeners();
	}

	public int getRectWid() {
		return Settings.RECT_WID;
	}

	public int getRectHei() {
		return Settings.RECT_WID;
	}

	class GridListeners implements MouseListener, MouseMotionListener {
		private Piece lastPressed;

		private boolean mouseHeld; // for knowing if the mouse is being held down
		private boolean movedFromUniquePiece; // its hard to press once without holding so had to add special case for it
															// (for startPiece and endPiece)

		GridListeners() {
			addMouseListener(this);
			addMouseMotionListener(this);
		}

		private Piece PressedPiece(int xPos, int yPos) {
			Rectangle2D rect;
			Piece piece = null;

			for (List<Piece> pieces : gridPieces) {
				for (Piece value : pieces) {
					rect = value.getRect();
					if (rect.contains(xPos, yPos)) {
						piece = value;
						return piece;
					}
				}
			}
			return null;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			mouseHeld = true;
			piecePressed(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			piecePressed(e);
		}

		/**
		 * TODO move this out
		 */
		private void piecePressed(MouseEvent e) {
			Piece pressed = PressedPiece(e.getX(), e.getY());
			if (e.getButton() != 1 && !mouseHeld || pressed == null)
				return;

			if (PressedPiece(e.getX(), e.getY()) == lastPressed)
				return;

			if (pressed.getType() == Piece.Type.Empty) {
				ifPieceEmpty(pressed);
			} else if (pressed.getType() == Piece.Type.Wall) {
				pressed.setType(Piece.Type.Empty);
				wasPreviousPieceUnique = null;
			} else if ((pressed.getType() == Piece.Type.Start || pressed.getType() == Piece.Type.End)
					&& wasPreviousPieceUnique == null && !mouseHeld) {
				wasPreviousPieceUnique = pressed;
				return;
			}

			addPiecesForRepainting(pressed);
			lastPressed = pressed;
		}

		/**
		 * TODO move this out
		 */
		// if the piece is empty and is being pressed do the following function
		private void ifPieceEmpty(Piece pressed) {
			if (wasPreviousPieceUnique == null) {
				pressed.setType(Piece.Type.Wall);
			} else if (!mouseHeld) {
				System.out.println(movedFromUniquePiece);
				pressed.setType(wasPreviousPieceUnique.getType());
				wasPreviousPieceUnique.setType(Piece.Type.Empty);

				addPiecesForRepainting(pressed, wasPreviousPieceUnique);

				if (pressed.getType() == Piece.Type.Start)
					startPiece = pressed;
				else if (pressed.getType() == Piece.Type.End)
					endPiece = pressed;

				wasPreviousPieceUnique = null;
			}
			// checking so it doesn't repaint the same piece while the cursor is held on the
			// piece that is unique (startpiece, endpiece)
			else if (lastPressed != pressed) {
				System.out.println("hello there " + movedFromUniquePiece);
				movedFromUniquePiece = true;

				pressed.setType(wasPreviousPieceUnique.getType());
				wasPreviousPieceUnique.setType(Piece.Type.Empty);

				addPiecesForRepainting(pressed, wasPreviousPieceUnique);

				if (pressed.getType() == Piece.Type.Start)
					startPiece = pressed;
				else if (pressed.getType() == Piece.Type.End)
					endPiece = pressed;

				wasPreviousPieceUnique = pressed;
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			lastPressed = null;
			wasPreviousPieceUnique = null;
			mouseHeld = false;
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {

		}

		@Override
		public void mouseMoved(MouseEvent e) {

		}

		@Override
		public void mouseClicked(MouseEvent e) {

		}
	}
}
