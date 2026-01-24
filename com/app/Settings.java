package com.app;

public class Settings {
    /**
     * CENTER_OFFSET: for some reason items inside the window don't want to align correctly so have to use offset to get approximation
     * GRID_OFFSET_N: offset in the x and y-axis from the window, how many units away from the top left corner of the window
     * GRID_WID: number of pieces on the X axis of the grid
     * GRID_HEI: number of pieces on the Y axis of the grid
     * RECT_WID: width of a single piece inside the grid, in pixels?
     * VISUALIZE_SPEED: speed between repainted pieces when repainting grid in millisecond
     * SHORTEST_VISUALIZE_SPEED: speed between repainted pieces when visualizing shortest path in milliseconds
     *
     * @apiNote using ridiculous values in GRID_WID/GRID_HEI and MOSTLY RECT_WID seems to result in visual glitches
     */

    public static final int CENTER_OFFSET = 12;
    public static final int WINDOW_WID = 900;
    public static final int WINDOW_HEI = 800;
    public static final int GRID_WID = 25;
    public static final int GRID_HEI = 25;
    public static final int BUTTON_WID = 150;
    public static final int BUTTON_HEI = 30;
    public static final int BUTTON_MARGIN = 5;
    public static final int RECT_WID = 10;
    public static final int VISUALIZE_SPEED = 15;
    public static final int SHORTEST_VISUALIZE_SPEED = 5;
    public static final int GRID_OFFSET_X = WINDOW_WID/2 - ((RECT_WID * GRID_WID)/2) - CENTER_OFFSET;
    public static final int GRID_OFFSET_Y = 150;
}
