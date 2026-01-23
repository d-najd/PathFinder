package com.app.ui;

import com.app.data.Piece;
import com.app.data.QueuePiece;
import com.app.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class DrawGrid extends JPanel {
    protected ArrayList<ArrayList<Piece>> gridPieces = new ArrayList<>();
    protected Piece startPiece;
    protected Piece endPiece;

    private boolean gridDrawn = false;

    private Piece wasPreviousPieceUnique; //by unique it means start or end position, because when you hold the left click and move the mouse the start position has to move with it
    public ArrayList<Piece> pieceForRepainting = new ArrayList<>();

    public DrawGrid() {
        setLayout(null);
        setBounds(Settings.GRID_OFFSET_X, Settings.GRID_OFFSET_Y, Settings.GRID_WID * getRectWid() + 1, Settings.GRID_HEI * getRectHei() + 1);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!pieceForRepainting.isEmpty()) {
            Graphics2D g2d = (Graphics2D) g;
            for (Piece tempPiece : pieceForRepainting) {
                g2d.setColor(tempPiece.getColor());
                g2d.fill(tempPiece.getRect());
                g2d.setColor(Color.black);
                g2d.draw(tempPiece.getRect());
            }
            pieceForRepainting.clear();
            //wait some time so it doesn't go tooo fast
            /*
            try {
                Thread.sleep(visualize_speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
             */
        } else if (!gridDrawn) {
            drawGrid((Graphics2D) g);
            gridDrawn = true;
            drawStartPositions();
        } else {
            Graphics2D g2d = (Graphics2D) g;
            for (ArrayList<Piece> pieces : gridPieces)
                for (Piece piece : pieces) {
                    g2d.setColor(piece.getColor());
                    g2d.fill(piece.getRect());
                    g2d.setColor(Color.black);
                    g2d.draw(piece.getRect());
                }
        }
    }

    private void drawStartPositions() {
        startPiece = gridPieces.getFirst().getFirst();
        startPiece.setType(Piece.Type.Start);
        endPiece = gridPieces.get(Settings.GRID_WID - 1).get(Settings.GRID_HEI - 1);
        endPiece.setType(Piece.Type.End);
        JPanel startPPanel = new JPanel();
        JPanel endPPanel = new JPanel();
        add(endPPanel);
        add(startPPanel);
        endPPanel.setBackground(Color.orange);
        startPPanel.setBackground(startPiece.getColor());
    }

    private void drawGrid(Graphics2D g) {
        if (!gridPieces.isEmpty())
            System.out.println("[ERROR] there are already pieces when creating the grid wtf?");

        for (int x = 0; x < Settings.GRID_WID; x++) {
            ArrayList<Piece> tempArr = new ArrayList<>();
            for (int y = 0; y < Settings.GRID_HEI; y++) {
                var newPiece = new Piece(x, y);
                g.setColor(Color.WHITE);
                g.fill(newPiece.getRect());
                g.setColor(Color.black);
                g.draw(newPiece.getRect());
                tempArr.add(newPiece);
            }
            gridPieces.add(tempArr);
        }
        new GridListeners(gridPieces, this);
    }

    @SuppressWarnings("BusyWait")
    public void drawShortestPath(ArrayList<QueuePiece> path) {
        for (int i = 1; i < path.size(); i++) {
            QueuePiece curPiece = path.get(i);

            gridPieces.get(curPiece.getX()).get(curPiece.getY()).setType(Piece.Type.DisplayingPath);//display shortest path type
            pieceForRepainting.add(gridPieces.get(curPiece.getX()).get(curPiece.getY()));
            paintImmediately(curPiece.getX() * Settings.RECT_WID, curPiece.getY() * Settings.RECT_WID, Settings.RECT_WID,
                    Settings.RECT_WID);
            try {
                Thread.sleep(Settings.SHORTEST_VISUALIZE_SPEED);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void clearBoard() {
        for (ArrayList<Piece> colPieceArr : gridPieces) {
            for (Piece curPiece : colPieceArr)
                if (curPiece.getType() == Piece.Type.Checked || curPiece.getType() == Piece.Type.DisplayingPath || curPiece.getType() == Piece.Type.Wall)
                    curPiece.setType(Piece.Type.Empty);
            pieceForRepainting.addAll(colPieceArr);
        }
        paintImmediately(0, 0, Settings.GRID_WID * Settings.RECT_WID,
                Settings.GRID_HEI * Settings.RECT_WID);
    }

    protected void clearPath() {
        for (ArrayList<Piece> colPieceArr : gridPieces) {
            for (Piece curPiece : colPieceArr)
                if (curPiece.getType() == Piece.Type.Checked || curPiece.getType() == Piece.Type.DisplayingPath)
                    curPiece.setType(Piece.Type.Empty);
            pieceForRepainting.addAll(colPieceArr);
        }
        paintImmediately(0, 0, Settings.GRID_WID * Settings.RECT_WID,
                Settings.GRID_HEI * Settings.RECT_WID);
    }

    public int getRectWid() {
        return Settings.RECT_WID;
    }

    public int getRectHei() {
        return Settings.RECT_WID;
    }

    class GridListeners implements MouseListener, MouseMotionListener {
        private ArrayList<ArrayList<Piece>> grid;
        private DrawGrid gridObj;
        private Piece lastPressed;

        private boolean mouseHeld; //for knowing if the mouse is being held down
        private boolean movedFromUniquePiece; //its hard to press once without holding so had to add special case for it (for startPiece and endPiece)

        GridListeners(ArrayList<ArrayList<Piece>> grid, DrawGrid gridObj) {
            this.grid = grid;
            this.gridObj = gridObj;
            gridObj.addMouseListener(this);
            gridObj.addMouseMotionListener(this);
        }

        private Piece PressedPiece(int xPos, int yPos) {
            Rectangle2D rect;
            Piece piece = null;

            for (ArrayList<Piece> pieces : grid) {
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
            } else if ((pressed.getType() == Piece.Type.Start || pressed.getType() == Piece.Type.End) && wasPreviousPieceUnique == null && !mouseHeld) {
                wasPreviousPieceUnique = pressed;
                return;
            }
            gridObj.pieceForRepainting.add(pressed);
            gridObj.repaint(pressed.getX() * Settings.RECT_WID, pressed.getY() * Settings.RECT_WID, Settings.RECT_WID,
                    Settings.RECT_WID);
            lastPressed = pressed;
        }

        //if the piece is empty and is being pressed do the following function
        private void ifPieceEmpty(Piece pressed) {
            if (wasPreviousPieceUnique == null) {
                pressed.setType(Piece.Type.Wall);
            } else if (!mouseHeld) {
                System.out.println(movedFromUniquePiece);
                pressed.setType(wasPreviousPieceUnique.getType());
                wasPreviousPieceUnique.setType(Piece.Type.Empty);

                pieceForRepainting.add(pressed);
                pieceForRepainting.add(wasPreviousPieceUnique);

                gridObj.paintImmediately(wasPreviousPieceUnique.getX() * Settings.RECT_WID, wasPreviousPieceUnique.getY() * Settings.RECT_WID, Settings.RECT_WID,
                        Settings.RECT_WID);
                gridObj.paintImmediately(pressed.getX() * Settings.RECT_WID, pressed.getY() * Settings.RECT_WID, Settings.RECT_WID,
                        Settings.RECT_WID);

                if (pressed.getType() == Piece.Type.Start)
                    startPiece = pressed;
                else if (pressed.getType() == Piece.Type.End)
                    endPiece = pressed;

                wasPreviousPieceUnique = null;
            }
            //checking so it doesn't repaint the same piece while the cursor is held on the piece that is unique (startpiece, endpiece)
            else if (lastPressed != pressed) {
                System.out.println("hello there " + movedFromUniquePiece);
                movedFromUniquePiece = true;

                pressed.setType(wasPreviousPieceUnique.getType());
                wasPreviousPieceUnique.setType(Piece.Type.Empty);

                pieceForRepainting.add(pressed);
                pieceForRepainting.add(wasPreviousPieceUnique);

                gridObj.paintImmediately(wasPreviousPieceUnique.getX() * Settings.RECT_WID, wasPreviousPieceUnique.getY() * Settings.RECT_WID, Settings.RECT_WID,
                        Settings.RECT_WID);
                gridObj.paintImmediately(pressed.getX() * Settings.RECT_WID, pressed.getY() * Settings.RECT_WID, Settings.RECT_WID,
                        Settings.RECT_WID);

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

            if (mouseHeld && !movedFromUniquePiece) {
                wasPreviousPieceUnique = null;
            }

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