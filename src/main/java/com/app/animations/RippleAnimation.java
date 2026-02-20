package com.app.animations;

import java.awt.*;
import java.util.*;

public class RippleAnimation {
	public static ArrayList<KeyFrame> getKeyframes(Rectangle from, Rectangle to) {
		ArrayList<KeyFrame> keyframes = new ArrayList<>();
		keyframes.add(new KeyFrame(0.0, from));
		// keyframes.add(_75(to));
		keyframes.add(new KeyFrame(1.0, to));

		return keyframes;
	}

	public static KeyFrame _75(Rectangle to) {
		var width = to.getWidth();
		var height = to.getHeight();

		double index = 1.1;
		int widthFinal = (int) Math.round(width * index);
		int heightFinal = (int) Math.round(height * index);
		int x = (int) Math.round(to.x);
		int y = (int) Math.round(to.y);
		return new KeyFrame(.75, new Rectangle(x, y, widthFinal, heightFinal));
	}

	public static KeyFrame _85(Rectangle to) {
		var width = to.getWidth();
		var height = to.getHeight();

		double index = 1.1;
		int widthFinal = (int) Math.round(width * index);
		int heightFinal = (int) Math.round(height * index);
		int x = (int) Math.round(to.x);
		int y = (int) Math.round(to.y);
		return new KeyFrame(.85, new Rectangle(x, y, widthFinal, heightFinal));
	}

	public static KeyFrame _90(Rectangle to) {
		var width = to.getWidth();
		var height = to.getHeight();

		double index = 0.95;
		int x = (int) Math.round(to.x);
		int y = (int) Math.round(to.y);
		int widthFinal = (int) Math.round(width * index);
		int heightFinal = (int) Math.round(height * index);
		return new KeyFrame(.9, new Rectangle(x, y, widthFinal, heightFinal));
	}
}
