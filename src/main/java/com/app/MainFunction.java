package com.app;

import javax.swing.JFrame;

import com.app.ui.ContentButtons;
import com.app.ui.DrawGrid;

public class MainFunction {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Pathfinding");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(Settings.WINDOW_WID, Settings.WINDOW_HEI);

		var grid = new DrawGrid();
		frame.getContentPane().add(grid);

		frame.getContentPane().add(new ContentButtons(grid));
		frame.setVisible(true);
	}
}
