package com.app;
import com.app.Algorithms.Bidirectional;
import com.app.Algorithms.BreadthFirst;
import com.app.Algorithms.Greedy;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainFunction{
    private static DrawGrid grid;
    private static JFrame frame;
    //number of elements in X and Y axis
    private static final int gridWid = Settings.GRID_WID;
    private static final int gridHei = Settings.GRID_HEI;
    private static final int centerX = Settings.WINDOW_WID/2 - Settings.CENTER_OFFSET;
    private static final int btnWid = Settings.BUTTON_WID;
    private static final int btnHei = Settings.BUTTON_HEI;

    private static MenuConstructor algorithmsMenu;
    private static String currentAlgorithm = "breadth";

    public static void main(String[] args){
        frame = new JFrame("Pathfinding");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Settings.WINDOW_WID, Settings.WINDOW_HEI);

        grid = new DrawGrid();
        grid.createAndShowGui(gridWid, gridHei);
        grid.setLayout(null);
        grid.setBounds(Settings.GRID_OFFSET_X, Settings.GRID_OFFSET_Y, gridWid * grid.getRectWid() + 1,
                gridHei * grid.getRectHei() + 1);
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
        button.setBounds((int) (centerX - btnWid * 2.5) - Settings.BUTTON_MARGIN * 2, 15, btnWid, btnHei);
        button.addActionListener(new ButtonListener());
        p.add(button);

        button = new JButton("Algorithms");
        button.setActionCommand("Algorithms");
        button.setBounds((int) (centerX - btnWid * 1.5) - Settings.BUTTON_MARGIN, 15, btnWid, btnHei);
        button.addActionListener(new ButtonListener());
        algorithmsMenuCon(p, button);
        p.add(button);

        button = new JButton("Visualize");
        button.setActionCommand("Visualize");
        button.setBounds(centerX - (btnWid/2), 15, btnWid, btnHei);
        button.addActionListener(new ButtonListener());
        p.add(button);

        button = new JButton("Clear Board");
        button.setActionCommand("ClearBoard");
        button.setBounds(centerX + (btnWid/2) + Settings.BUTTON_MARGIN,15, btnWid, btnHei);
        button.addActionListener(new ButtonListener());
        p.add(button);

        button = new JButton("Clear Path");
        button.setActionCommand("ClearPath");
        button.setBounds((int) (centerX + btnWid * 1.5 + Settings.BUTTON_MARGIN * 2),15, btnWid, btnHei);
        button.addActionListener(new ButtonListener());
        p.add(button);

        return p;
    }

    private static void algorithmsMenuCon(JPanel p, JButton rootButton) {
        ArrayList<JButton> algorithmsList = new ArrayList<>();
        JButton button;

        button = new JButton("Breadth first");
        button.setActionCommand("algorithm_breadth");
        button.addActionListener(new ButtonListener());
        algorithmsList.add(button);

        button = new JButton("Greedy best first");
        button.setActionCommand("algorithm_greedy");
        button.addActionListener(new ButtonListener());
        algorithmsList.add(button);

        button = new JButton("Bidirectional swarm");
        button.setActionCommand("algorithm_bi_swarm");
        button.addActionListener(new ButtonListener());
        algorithmsList.add(button);

        algorithmsMenu = new MenuConstructor(p, rootButton, algorithmsList, null);
    }

    static class ButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand().toLowerCase()){
                case "algorithm_breadth":
                    currentAlgorithm = "breadth";
                    break;
                case "algorithm_greedy":
                    currentAlgorithm = "greedy";
                    break;
                case "algorithm_bi_swarm":
                    currentAlgorithm = "bi_swarm";
                    break;
                case "algorithms":
                    algorithmsMenu.swapState();
                    break;
                case "visualize":
                    if (currentAlgorithm.equals("breadth")) {
                        BreadthFirst.start(grid.startPiece, grid.gridPieces, grid, Settings.VISUALIZE_SPEED);
                    }
                    else if (currentAlgorithm.equals("greedy")){
                        Greedy.start(grid.startPiece, grid.endPiece, grid.gridPieces, grid, Settings.VISUALIZE_SPEED);
                    }
                    else if (currentAlgorithm.equals("bi_swarm")) {
                        Bidirectional.start(grid.startPiece, grid.endPiece, grid.gridPieces, grid, Settings.VISUALIZE_SPEED);
                    }
                    else{
                        System.out.println("[ERROR] current algorithm doesn't exist, algorithm: " + currentAlgorithm);
                        break;
                    }
                    System.out.println("Visualize baby");
                    break;
                case "clearboard":
                    grid.ClearBoard();
                    break;
                case "clearpath":
                    grid.ClearPath();
                    break;
                default:
                    System.out.println("[ERROR] the current button has not been defined, its action command is: " + e.getActionCommand());
            }
            /*
            if (e.getActionCommand().equals("Visualize"))
            {
                BreadthFirst.Start(grid.startPiece, grid.gridPieces, grid);
                System.out.println("Visualize baby");
            } else
                System.out.println("the current button has not been defined, its action command is: " + e.getActionCommand());

             */
        }
    }
}