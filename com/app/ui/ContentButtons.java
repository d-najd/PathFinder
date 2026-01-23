package com.app.ui;

import com.app.algorithms.*;
import com.app.Settings;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContentButtons extends JPanel {
    private static SearchAlgorithm runningAlgorithm = null;

    public static SearchAlgorithm getRunningAlgorithm() {
        return runningAlgorithm;
    }

    private static final int centerX = Settings.WINDOW_WID / 2 - Settings.CENTER_OFFSET;

    private MenuConstructor algorithmsMenu;
    private SearchAlgorithm currentAlgorithm = SearchAlgorithm.BreadthFirst;
    private final ISearchAlgorithm[] searchAlgorithms = { new Bidirectional(), new BreadthFirst(), new Greedy() };

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final DrawGrid drawGrid;

    public ContentButtons(DrawGrid drawGrid) {
        this.drawGrid = drawGrid;

        createButtons();
    }

    public void createButtons() {
        setLayout(null);
        JButton button;

        button = new JButton("Mazes & Patterns");
        button.setBounds((int) (centerX - Settings.BUTTON_WID * 2.5) - Settings.BUTTON_MARGIN * 2, 15, Settings.BUTTON_WID, Settings.BUTTON_HEI);
        add(button);

        button = new JButton("Algorithms");
        button.setBounds((int) (centerX - Settings.BUTTON_WID * 1.5) - Settings.BUTTON_MARGIN, 15, Settings.BUTTON_WID, Settings.BUTTON_HEI);
        button.addActionListener(_ -> algorithmsMenu.swapState());
        add(button);

        algorithmsDropdownMenu(button);

        button = new JButton("Visualize");
        button.setBounds(centerX - (Settings.BUTTON_WID / 2), 15, Settings.BUTTON_WID, Settings.BUTTON_HEI);
        button.addActionListener(_ -> {
            var matching = Arrays.stream(searchAlgorithms).filter(o -> o.currentAlgorithm() == currentAlgorithm).findFirst();
            if (matching.isEmpty()) {
                throw new IllegalStateException();
            }
            runningAlgorithm = SearchAlgorithm.BreadthFirst;

            executor.submit(() -> {
                matching.get().start(drawGrid.startPiece, drawGrid.endPiece, drawGrid.gridPieces, drawGrid, Settings.VISUALIZE_SPEED, ContentButtons::getRunningAlgorithm);
            });
        });
        add(button);

        button = new JButton("Clear Board");
        button.setBounds(centerX + (Settings.BUTTON_WID / 2) + Settings.BUTTON_MARGIN, 15, Settings.BUTTON_WID, Settings.BUTTON_HEI);
        button.addActionListener(_ -> {
            runningAlgorithm = null;

            drawGrid.clearBoard();
        });
        add(button);

        button = new JButton("Clear Path");
        button.setBounds((int) (centerX + Settings.BUTTON_WID * 1.5 + Settings.BUTTON_MARGIN * 2), 15, Settings.BUTTON_WID, Settings.BUTTON_HEI);
        button.addActionListener(_ -> drawGrid.clearPath());
        add(button);
    }

    public void algorithmsDropdownMenu(JButton rootButton) {
        ArrayList<JButton> algorithmsList = new ArrayList<>();
        JButton button;

        button = new JButton("Breadth first");
        button.addActionListener(_ -> currentAlgorithm = SearchAlgorithm.BreadthFirst);
        algorithmsList.add(button);

        button = new JButton("Greedy");
        button.addActionListener(_ -> currentAlgorithm = SearchAlgorithm.Greedy);
        algorithmsList.add(button);

        button = new JButton("Bidirectional swarm");
        button.addActionListener(_ -> currentAlgorithm = SearchAlgorithm.Bidirectional);
        algorithmsList.add(button);

        algorithmsMenu = new MenuConstructor(this, rootButton, algorithmsList, null);
    }

}
