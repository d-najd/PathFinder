package com.app.ui;

import com.app.algorithms.*;
import com.app.mazes.IMaze;
import com.app.mazes.Maze;
import com.app.mazes.RandomMaze;
import com.app.mazes.RecursiveDivisionMaze;
import com.app.mazes.StarMaze;
import com.app.Settings;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContentButtons extends JPanel {
	private static SearchAlgorithm runningAlgorithm = null;
	private static Maze runningMaze = null;

	public static Maze getRunningMaze() {
		return runningMaze;
	}

	public static SearchAlgorithm getRunningAlgorithm() {
		return runningAlgorithm;
	}

	public static boolean isSomethingRunning() {
		if (getRunningAlgorithm() != null || getRunningMaze() != null) {
			return true;
		}
		return false;
	}

	private static final int centerX = Settings.WINDOW_WID / 2 - Settings.CENTER_OFFSET;

	private MenuConstructor algorithmsMenu;
	private MenuConstructor mazesMenu;
	private SearchAlgorithm selectedAlgorithm = SearchAlgorithm.BreadthFirst;
	private final ISearchAlgorithm[] searchAlgorithms = { new Bidirectional(), new BreadthFirst(), new Greedy(),
			new DepthFirst() };
	private final IMaze[] mazes = { new RandomMaze(), new StarMaze(), new RecursiveDivisionMaze() };

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
		button.setBounds((int) (centerX - Settings.BUTTON_WID * 2.5) - Settings.BUTTON_MARGIN * 2, 15,
				Settings.BUTTON_WID,
				Settings.BUTTON_HEI);
		button.addActionListener(o -> {
			if (isSomethingRunning()) {
				return;
			}
			mazesMenu.swapState();
		});
		add(button);

		mazesDropdownMenu(button);

		button = new JButton("Algorithms");
		button.setBounds((int) (centerX - Settings.BUTTON_WID * 1.5) - Settings.BUTTON_MARGIN, 15, Settings.BUTTON_WID,
				Settings.BUTTON_HEI);
		button.addActionListener(o -> {
			if (isSomethingRunning()) {
				return;
			}
			algorithmsMenu.swapState();
		});
		add(button);

		algorithmsDropdownMenu(button);

		button = new JButton("Visualize");
		button.setBounds(centerX - (Settings.BUTTON_WID / 2), 15, Settings.BUTTON_WID, Settings.BUTTON_HEI);
		button.addActionListener(m -> {
			if (isSomethingRunning()) {
				return;
			}

			var matching = Arrays.stream(searchAlgorithms).filter(o -> o.currentAlgorithm() == selectedAlgorithm)
					.findFirst();
			if (matching.isEmpty()) {
				throw new IllegalStateException();
			}
			runningAlgorithm = selectedAlgorithm;

			executor.submit(() -> {
				matching.get().start(drawGrid.startPiece, drawGrid.endPiece, drawGrid.gridPieces, drawGrid,
						ContentButtons::getRunningAlgorithm);
			});
		});
		add(button);

		button = new JButton("Clear Board");
		button.setBounds(centerX + (Settings.BUTTON_WID / 2) + Settings.BUTTON_MARGIN, 15, Settings.BUTTON_WID,
				Settings.BUTTON_HEI);
		button.addActionListener(o -> {
			runningAlgorithm = null;
			runningMaze = null;
			drawGrid.clearBoard();
		});
		add(button);

		button = new JButton("Clear Path");
		button.setBounds((int) (centerX + Settings.BUTTON_WID * 1.5 + Settings.BUTTON_MARGIN * 2), 15,
				Settings.BUTTON_WID,
				Settings.BUTTON_HEI);
		button.addActionListener(o -> {
			runningAlgorithm = null;
			drawGrid.clearPath();
		});
		add(button);
	}

	public void mazesDropdownMenu(JButton rootButton) {
		ArrayList<JButton> mazesList = new ArrayList<>();
		JButton button;

		button = new JButton("Random Maze");
		button.addActionListener(o -> submitMaze(Maze.Random));
		mazesList.add(button);

		// button = new JButton("Star Pattern");
		// button.addActionListener(o -> submitMaze(Maze.Star));
		// mazesList.add(button);

		button = new JButton("Recursive Maze");
		button.addActionListener(o -> submitMaze(Maze.RecursiveDevision));
		mazesList.add(button);

		mazesMenu = new MenuConstructor(this, rootButton, mazesList, null);
	}

	private void submitMaze(Maze mazeType) {
		var matching = Arrays.stream(mazes).filter(o -> o.currentMaze() == mazeType).findFirst();
		if (matching.isEmpty()) {
			throw new IllegalStateException();
		}

		runningMaze = mazeType;

		executor.submit(() -> {
			try {
				drawGrid.clearBoard();
				matching.get().generateMaze(drawGrid.gridPieces, drawGrid, ContentButtons::getRunningMaze);
				runningMaze = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public void algorithmsDropdownMenu(JButton rootButton) {
		ArrayList<JButton> algorithmsList = new ArrayList<>();
		JButton button;

		button = new JButton("Breadth first");
		button.addActionListener(o -> selectedAlgorithm = SearchAlgorithm.BreadthFirst);
		algorithmsList.add(button);

		button = new JButton("Depth first");
		button.addActionListener(o -> selectedAlgorithm = SearchAlgorithm.DepthFirst);
		algorithmsList.add(button);

		button = new JButton("Greedy");
		button.addActionListener(o -> selectedAlgorithm = SearchAlgorithm.Greedy);
		algorithmsList.add(button);

		button = new JButton("Bidirectional swarm");
		button.addActionListener(o -> selectedAlgorithm = SearchAlgorithm.Bidirectional);
		algorithmsList.add(button);

		algorithmsMenu = new MenuConstructor(this, rootButton, algorithmsList, null);
	}
}
