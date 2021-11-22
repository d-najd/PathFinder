import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javafx.util.Pair;
import processing.core.*;

public class Display {
    // Colors used for empty locations.

    // Color used for objects that have no defined color.
    private static int EMPTY_COLOR;

    private PApplet p; // the applet we want to display on

    private int x, y, w, h; // (x, y) of upper left corner of display
    // the width and height of the display

    private float dx, dy; // calculate the width and height of each box
    // in the field display using the size of the field
    // and the width and height of the display

    private int rows, cols;

    // A map for storing colors for participants in the simulation
    private Map<Object, Integer> colors;
    private Map<Object, PImage> images;

    // (x, y) is the upper-left corner of the display in pixels
    // w and h are the width and height of the display in pixels
    public Display(PApplet p, int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.p = p;

        EMPTY_COLOR = p.color(255, 255, 255);

        colors = new LinkedHashMap<Object, Integer>();
        images = new LinkedHashMap<Object, PImage>();
    }

    public void drawGrid(int[][] grid) {
        int piece;
        int numcols = grid[0].length;
        int numrows = grid.length;

        for (int i = 0; i < numrows; i++) {
            for (int j = 0; j < numcols; j++) {
                piece = grid[i][j];
                int pieceColor = getColor(piece);
                PImage pieceImage = getImage(piece);

                if (pieceImage != null) {
                    p.image(pieceImage, x + j * dx, y + i * dy, dx, dy);
                } else {
                    p.fill(getColor(piece));
                    p.rect(x + j * dx, y + i * dy, dx, dy);
                }
            }
        }
    }

    public ArrayList<Pair> DrawButtons(){
        ArrayList<Pair> buttons = new ArrayList<>();
        ArrayList<ButtonInstance> buttonInstances = new ArrayList<>();

        int r = 100, g = 100, b = 100; //default color for all buttons

        int btnPosX = 620, btnPosY = 10, btnWid = 75, btnHei = 20;
        //buttonInstances.add(new ButtonInstance());
        buttons.add(new Pair(new int[] {btnPosX, btnPosY, btnWid, btnHei, r, g, b}, "Simulate"));

        for (int i = 0; i < buttons.size(); i++){
            int[] values = (int[]) buttons.get(i).getKey();
            String name = (String) buttons.get(i).getValue();

            p.fill(p.color(values[4], values[5], values[6]));
            p.rect(values[0], values[1], values[2], values[3]);

            if(name != null)
            p.text(name, values[0], values[1]);
        }

        return buttons;
    }



    /**
     * Define a color to be used for a given value in the grid.
     *
     * @param pieceType The type of piece in the grid.
     * @param color     The color to be used for the given type of piece.
     */
    public void setColor(Object pieceType, Integer color) {
        colors.put(pieceType, color);
    }

    /**
     * Define an Image to be used for a given value in the grid.
     *
     * @param pieceType The type of piece in the grid.
     * @param img       The image to be used for the given type of piece.
     */
    public void setImage(Object pieceType, PImage img) {
        images.put(pieceType, img);
    }

    /**
     * Define a color to be used for a given value in the grid.
     *
     * @param pieceType The type of piece in the grid.
     * @param filename  The file path to the image to be used for the given type of piece.
     */
    public void setImage(Object pieceType, String filename) {
        PImage img = p.loadImage(filename);
        setImage(pieceType, img);
    }

    /**
     * @return The color to be used for a given class of animal.
     */
    private Integer getColor(Object pieceType) {
        Integer col = colors.get(pieceType);
        if (col == null) { // no color defined for this class
            return EMPTY_COLOR;
        } else {
            return col;
        }
    }

    private PImage getImage(Object pieceType) {
        PImage img = images.get(pieceType);
        return img;
    }


    // Return location at coordinates x, y on the screen
    public Location gridLocationAt(float mousex, float mousey) {
        Location l = new Location((int) Math.floor((mousey - y) / dy),
                (int) Math.floor((mousex - x) / dx));
        return l;
    }

    public void setNumCols(int numCols) {
        rows = numCols;
        dx = w / rows;
    }

    public void setNumRows(int numRows) {
        cols = numRows;
        dy = h / cols;
    }

    public void initializeWithGame(Board game) {
        int[][] grid = game.getGrid();
        if (grid == null) {
            System.out
                    .println("Your 2d int array grid is null!  Create it by saying grid = new int[___][___] inside your constructor!");
        }
        setNumCols(grid[0].length);
        setNumRows(grid.length);
        System.out.println("Setting disply: # rows is " + grid.length + ", # cols is " + grid[0].length);
    }

    // Draw a box at the location
    public void DrawBoxAtPos(Location l, Board g) {
        if (g.isInGrid(l.getRow(),l.getCol())) {
            p.fill(p.color(50, 200, 50, 150));
            p.rect(xCoordOf(l), yCoordOf(l), dx, dy);
        }
    }

    // return the y pixel value of the upper-left corner of location l
    private float yCoordOf(Location l) {
        return y + l.getRow() * dy;
    }

    // return the x pixel value of the upper-left corner of location l
    private float xCoordOf(Location l) {
        return x + l.getCol() * dx;
    }


    public void displayTextOnGrid(int[][] grid, int fontsize, int horizontal_adjust, int vertical_adjust) {
        int text;
        int numcols = grid[0].length;
        int numrows = grid.length;

        p.textSize(fontsize);

        for (int i = 0; i < numrows; i++) {
            for (int j = 0; j < numcols; j++) {
                text = grid[i][j];

                p.fill(p.color(0, 0, 0));   // display text in black
                p.textMode(p.CENTER);
                p.text(text, x + j * dx + dx/2 - fontsize/2 + horizontal_adjust,
                        y + i * dy + dy/2 + fontsize/2 + vertical_adjust);
            }
        }
    }

    public void displayTextOnGrid(String[][] grid, int fontsize) {
        displayTextOnGrid(grid, fontsize, 0, 0);
    }

    public void displayTextOnGrid(String[][] grid, int fontsize, int horizontal_adjust, int vertical_adjust) {
        String text;
        int numcols = grid[0].length;
        int numrows = grid.length;

        p.textSize(fontsize);

        for (int i = 0; i < numrows; i++) {
            for (int j = 0; j < numcols; j++) {
                text = grid[i][j];

                p.fill(p.color(0, 0, 0));   // display text in black
                p.textMode(p.CENTER);
                p.text(text, x + j * dx + dx/2 - fontsize/2 + horizontal_adjust,
                        y + i * dy + dy/2 + fontsize/2 + vertical_adjust);
            }
        }
    }

    public void displayTextOnGrid(int[][] grid, int fontsize) {
        displayTextOnGrid(grid, fontsize, 0, 0);
    }
}