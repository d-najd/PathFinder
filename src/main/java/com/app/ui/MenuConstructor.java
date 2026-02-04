package com.app.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MenuConstructor {
	private Boolean state = false; // false for invisible, true for visible
	private final ArrayList<JButton> buttonList = new ArrayList<>();

	private final JPanel panel;
	private final JButton menuButton;

	/**
	 * basic constructor for menu under the button,
	 * 
	 * @apiNote the position of the buttons is handled here everything else needs to
	 *          be handled by the
	 * @param panel           the panel where the menu is located
	 * @param menuButton      the button which the menu is located under
	 * @param inputButtonList list of the buttons
	 * @param tag             a tag for special some special action
	 */
	public MenuConstructor(JPanel panel, JButton menuButton, ArrayList<JButton> inputButtonList, String tag) {
		this.panel = panel;
		this.menuButton = menuButton;

		closeOnPressedOutside();

		for (int i = 0; i < inputButtonList.size(); i++) {
			JButton curButton = inputButtonList.get(i);
			Rectangle bounds = menuButton.getBounds();

			curButton.setBounds(bounds.x, bounds.height * (1 + i) + bounds.y, bounds.width, bounds.height);
			curButton.setVisible(state);
			panel.add(curButton);
			buttonList.add(curButton);
		}
	}

	/**
	 * for switching the state on and off
	 */
	public void swapState() {
		state = !state;
		for (JButton button : buttonList) {
			button.setVisible(state);
		}
	}

	private void closeOnPressedOutside() {
		long eventMask = AWTEvent.MOUSE_MOTION_EVENT_MASK + AWTEvent.MOUSE_EVENT_MASK;
		Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
			if (!state) {
				return;
			}

			if (!(event instanceof MouseEvent me && me.getID() == MouseEvent.MOUSE_CLICKED)) {
				return;
			}

			if (menuButton.contains(me.getPoint())) {
				System.out.println("CONTAINS ");
				return;
			}

			swapState();

		}, eventMask);
	}
}
