package com.app;

import com.app.Animations.Animator;
import com.app.Animations.AnimatorHelper;
import com.app.Objects.Piece;
import com.app.Objects.QueuePiece;

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
    public int visualize_speed = 0;

    private boolean gridDrawn = false;
    private int xAxisPieces;
    private int yAxisPieces;

    private final int rectWid = Settings.RECT_WID;
    private final int rectHei = rectWid;
    private final int rectX = rectWid;
    private final int rectY = rectHei;

    private Piece wasPreviousPieceUnique; //by unique it means start or end position, because when you hold the left click and move the mouse the start position has to move with it
    public ArrayList<Piece> pieceForRepainting = new ArrayList<>();

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
            try {
                Thread.sleep(visualize_speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else if (!gridDrawn) {
            DrawGrid(g);
            gridDrawn = true;
            drawStartPositions(g);
        } else {
            //System.out.println("[WARRNING] um trying to repaint something but there is nothing to repaint?");
            Graphics2D g2d = (Graphics2D) g;
            for (ArrayList<Piece> pieces : gridPieces)
                for (Piece piece : pieces)
                {
                    g2d.setColor(piece.getColor());
                    g2d.fill(piece.getRect());
                    g2d.setColor(Color.black);
                    g2d.draw(piece.getRect());
                }
        }

        /**
         * @NOTE repainting the grid using DrawGrid is a bad idea because it will paint another grid on top of the first
         * one and add the elements to
         * @param pieces which is a really bad idea and will cause a memory leak
         */
    }

    private void oldDrawStartPositions(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Rectangle2D tempRect;

        //it seems I took startPiece out of here accidentally

        endPiece = gridPieces.get(xAxisPieces - 1).get(yAxisPieces - 1);
        endPiece.setType(Piece.Type.End);
        tempRect = endPiece.getRect();
        g2d.setColor(endPiece.getColor());
        g2d.fill(tempRect);
        g2d.setColor(Color.black);
        g2d.draw(tempRect);
    }

    private void drawStartPositions(Graphics g) {
        //initial setup
        startPiece = gridPieces.get(0).get(0);
        startPiece.setType(Piece.Type.Start);
        endPiece = gridPieces.get(xAxisPieces - 1).get(yAxisPieces - 1);
        endPiece.setType(Piece.Type.End);
        JPanel startPPanel = new JPanel();
        JPanel endPPanel = new JPanel();
        add(endPPanel);
        add(startPPanel);
        endPPanel.setBackground(Color.orange);
        startPPanel.setBackground(startPiece.getColor());
        //endPiece.setStartType(Piece.Type.Empty);

        /*
        Rectangle from = AnimatorHelper.calculateCenter(endPiece);
        Rectangle to = AnimatorHelper.calculateEndPos(endPiece);

        Animator animator = new Animator(endPPanel, from, to, 5000);
        animator.ripple();
         */

        /*
        Rectangle from = new Rectangle((Settings.RECT_WID - startPiece.getX() / 2 ) / 2,
                (Settings.RECT_WID - startPiece.getY()/2) / 2, 0, 0);
        Rectangle to = new Rectangle(startPiece.getX() + 1,
                startPiece.getY() + 1, Settings.RECT_WID - 1, Settings.RECT_WID - 1);

        Animator animator = new Animator(startPPanel, from, to, 1000);
        animator.RippleAnimation();
         */


        //AnimatorOld.Animate animate2 = new AnimatorOld.Animate(subPanel, from, to);
        //animate2.start();
    }

    private ArrayList<ArrayList<Piece>> DrawGrid(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        if (!gridPieces.isEmpty())
            System.out.println("[ERROR] there are already pieces when creating the grid wtf?");

        for (int x = 0; x < xAxisPieces; x++) {
            ArrayList<Piece> tempArr = new ArrayList<>();
            for (int y = 0; y < yAxisPieces; y++) {
                var newPiece = new Piece(x, y);
                g2d.setColor(Color.WHITE);
                g2d.fill(newPiece.getRect());
                g2d.setColor(Color.black);
                g2d.draw(newPiece.getRect());
                tempArr.add(newPiece);
            }
            gridPieces.add(tempArr);
        }
        new GridListeners(gridPieces, this);
        return gridPieces;
    }

    // create the GUI explicitly on the Swing event thread
    public void createAndShowGui(int xAxisPieces_, int yAxisPieces_) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                xAxisPieces = xAxisPieces_;
                yAxisPieces = yAxisPieces_;
            }
        });
    }

    public void DrawShortestPath(ArrayList<QueuePiece> path){
        for (int i = 1; i < path.size(); i++){
            QueuePiece curPiece = path.get(i);

            gridPieces.get(curPiece.getX()).get(curPiece.getY()).setType(Piece.Type.DisplayingPath);//display shortest path type
            pieceForRepainting.add(gridPieces.get(curPiece.getX()).get(curPiece.getY()));
            paintImmediately(curPiece.getX() * rectWid, curPiece.getY() * rectHei, rectWid,
                    rectHei);
            try {
                Thread.sleep(Settings.SHORTEST_VISUALIZE_SPEED);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void ClearBoard(){
        visualize_speed = 0;

        for (ArrayList<Piece> colPieceArr : gridPieces){
            for (Piece curPiece : colPieceArr)
                if (curPiece.getType() == Piece.Type.Checked || curPiece.getType() == Piece.Type.DisplayingPath || curPiece.getType() == Piece.Type.Wall)
                    curPiece.setType(Piece.Type.Empty);
            pieceForRepainting.addAll(colPieceArr);
        }
        paintImmediately(0, 0, Settings.GRID_WID * rectWid,
                Settings.GRID_HEI * rectHei);

    }

    protected void ClearPath(){
        visualize_speed = 0;

        for (ArrayList<Piece> colPieceArr : gridPieces){
            for (Piece curPiece : colPieceArr)
                if (curPiece.getType() == Piece.Type.Checked || curPiece.getType() == Piece.Type.DisplayingPath)
                    curPiece.setType(Piece.Type.Empty);
            pieceForRepainting.addAll(colPieceArr);
        }
        paintImmediately(0, 0, Settings.GRID_WID * rectWid,
                Settings.GRID_HEI * rectHei);
    }

    public int getRectWid(){
        return rectWid;
    }

    public int getRectHei(){
        return rectHei;
    }

    class GridListeners implements MouseListener, MouseMotionListener{
        private ArrayList<ArrayList<Piece>> grid;
        private DrawGrid gridObj;
        private Piece lastPressed;

        private boolean mouseHeld; //for knowing if the mouse is being held down
        private boolean movedFromUniquePiece; //its hard to press once without holding so had to add special case for it (for startPiece and endPiece)

        GridListeners(ArrayList<ArrayList<Piece>> grid, DrawGrid gridObj){
            this.grid = grid;
            this.gridObj = gridObj;
            gridObj.addMouseListener(this);
            gridObj.addMouseMotionListener(this);
        }

        private Piece PressedPiece(int xPos, int yPos){
            Rectangle2D rect;
            Piece piece = null;

            for (int x = 0; x < grid.size(); x++){
                for (int y = 0; y < grid.get(x).size(); y++){
                    rect = grid.get(x).get(y).getRect();
                    if (rect.contains(xPos, yPos)){
                        piece = grid.get(x).get(y);
                        return piece;
                    }
                }
            }
            return null;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            mouseHeld = true;
            PiecePressed(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            PiecePressed(e);
        }

        private void PiecePressed(MouseEvent e) {
            Piece pressed = PressedPiece(e.getX(), e.getY());
            if (e.getButton() != 1 && !mouseHeld || pressed == null)
                return;

            if (PressedPiece(e.getX(), e.getY()) == lastPressed)
                return;

            if (pressed.getType() == Piece.Type.Empty) {
                IfPieceEmpty(pressed);
            } else if (pressed.getType() == Piece.Type.Wall) {
                pressed.setType(Piece.Type.Empty);
                wasPreviousPieceUnique = null;
            } else if ((pressed.getType() == Piece.Type.Start || pressed.getType() == Piece.Type.End) && wasPreviousPieceUnique == null && !mouseHeld){
                wasPreviousPieceUnique = pressed;
                return;
            }
            gridObj.pieceForRepainting.add(pressed);
            gridObj.repaint(pressed.getX() * gridObj.rectWid, pressed.getY() * gridObj.rectHei, gridObj.rectWid,
                    gridObj.rectHei);
            lastPressed = pressed;
        }

        //if the piece is empty and is being pressed do the following function
        private void IfPieceEmpty(Piece pressed) {
            if (wasPreviousPieceUnique == null) {
                pressed.setType(Piece.Type.Wall);
            }
            else if (!mouseHeld)
            {
                System.out.println(movedFromUniquePiece);
                pressed.setType(wasPreviousPieceUnique.getType());
                wasPreviousPieceUnique.setType(Piece.Type.Empty);

                pieceForRepainting.add(pressed);
                pieceForRepainting.add(wasPreviousPieceUnique);

                gridObj.paintImmediately(wasPreviousPieceUnique.getX() * gridObj.rectWid, wasPreviousPieceUnique.getY() * gridObj.rectHei, gridObj.rectWid,
                        gridObj.rectHei);
                gridObj.paintImmediately(pressed.getX() * gridObj.rectWid, pressed.getY() * gridObj.rectHei, gridObj.rectWid,
                        gridObj.rectHei);

                if (pressed.getType() == Piece.Type.Start)
                    startPiece = pressed;
                else if (pressed.getType() == Piece.Type.End)
                    endPiece = pressed;

                wasPreviousPieceUnique = null;
            }
            //checking so it doesn't repaint the same piece while the cursor is held on the piece that is unique (startpiece, endpiece)
            else if (lastPressed != pressed){
                System.out.println("hello there " + movedFromUniquePiece);
                movedFromUniquePiece = true;

                pressed.setType(wasPreviousPieceUnique.getType());
                wasPreviousPieceUnique.setType(Piece.Type.Empty);

                pieceForRepainting.add(pressed);
                pieceForRepainting.add(wasPreviousPieceUnique);

                gridObj.paintImmediately(wasPreviousPieceUnique.getX() * gridObj.rectWid, wasPreviousPieceUnique.getY() * gridObj.rectHei, gridObj.rectWid,
                        gridObj.rectHei);
                gridObj.paintImmediately(pressed.getX() * gridObj.rectWid, pressed.getY() * gridObj.rectHei, gridObj.rectWid,
                        gridObj.rectHei);

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