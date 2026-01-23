package com.app;

import com.app.Algorithms.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MainFunction {
    private static DrawGrid grid;
    //number of elements in X and Y axis
    private static final int centerX = Settings.WINDOW_WID / 2 - Settings.CENTER_OFFSET;

    private static MenuConstructor algorithmsMenu;
    private static SearchAlgorithm currentAlgorithm = SearchAlgorithm.BreadthFirst;
    private static final ISearchAlgorithm[] searchAlgorithms = {new Bidirectional(), new BreadthFirst(), new Greedy()};

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pathfinding");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Settings.WINDOW_WID, Settings.WINDOW_HEI);

        grid = new DrawGrid();
        grid.setLayout(null);
        grid.setBounds(Settings.GRID_OFFSET_X, Settings.GRID_OFFSET_Y, Settings.GRID_WID * grid.getRectWid() + 1,
                Settings.GRID_HEI * grid.getRectHei() + 1);
        frame.getContentPane().add(grid);

        frame.getContentPane().add(createButtons());
        frame.setVisible(true);
    }

    private static JPanel createButtons() {
        JPanel p = new JPanel();
        p.setLayout(null);
        JButton button;

        button = new JButton("Mazes & Patterns");
        button.setActionCommand("Mazes & Patterns");
        button.setBounds((int) (centerX - Settings.BUTTON_WID * 2.5) - Settings.BUTTON_MARGIN * 2, 15, Settings.BUTTON_WID, Settings.BUTTON_HEI);
        p.add(button);

        button = new JButton("Algorithms");
        button.setBounds((int) (centerX - Settings.BUTTON_WID * 1.5) - Settings.BUTTON_MARGIN, 15, Settings.BUTTON_WID, Settings.BUTTON_HEI);
        button.addActionListener(_ -> algorithmsMenu.swapState());
        p.add(button);

        algorithmsDropdownMenu(p, button);

        button = new JButton("Visualize");
        button.setActionCommand("Visualize");
        button.setBounds(centerX - (Settings.BUTTON_WID / 2), 15, Settings.BUTTON_WID, Settings.BUTTON_HEI);
        button.addActionListener(_ -> {
            var matching = Arrays.stream(searchAlgorithms).filter(o -> o.currentAlgorithm() == currentAlgorithm).findFirst();
            if (matching.isEmpty()) {
                throw new IllegalStateException();
            }
            matching.get().start(grid.startPiece, grid.endPiece, grid.gridPieces, grid, Settings.VISUALIZE_SPEED);
        });
        p.add(button);

        button = new JButton("Clear Board");
        button.setBounds(centerX + (Settings.BUTTON_WID / 2) + Settings.BUTTON_MARGIN, 15, Settings.BUTTON_WID, Settings.BUTTON_HEI);
        button.addActionListener(_ -> grid.ClearBoard());
        p.add(button);

        button = new JButton("Clear Path");
        button.setActionCommand("ClearPath");
        button.setBounds((int) (centerX + Settings.BUTTON_WID * 1.5 + Settings.BUTTON_MARGIN * 2), 15, Settings.BUTTON_WID, Settings.BUTTON_HEI);
        button.addActionListener(_ -> grid.ClearPath());
        p.add(button);

        return p;
    }

    private static void algorithmsDropdownMenu(JPanel p, JButton rootButton) {
        ArrayList<JButton> algorithmsList = new ArrayList<>();
        JButton button;

        button = new JButton("Breadth first");
        button.addActionListener(_ -> currentAlgorithm = SearchAlgorithm.BreadthFirst);
        algorithmsList.add(button);

        button = new JButton("Greedy best first");
        button.addActionListener(_ -> currentAlgorithm = SearchAlgorithm.Greedy);
        algorithmsList.add(button);

        button = new JButton("Bidirectional swarm");
        button.addActionListener(_ -> currentAlgorithm = SearchAlgorithm.Bidirectional);
        algorithmsList.add(button);

        algorithmsMenu = new MenuConstructor(p, rootButton, algorithmsList, null);
    }
}