package com.app.animations;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import com.app.data.Piece;

public class Animator {
	public static Rectangle ripple(Piece piece) {
		var from = AnimatorHelper.calculateCenter(piece);
		var to = AnimatorHelper.calculateEndPos(piece);

		return ripple(from, to, piece.getAnimationPercentage());
	}

	public static Rectangle ripple(Rectangle from, Rectangle to, double percentage) {
		var keyframes = RippleAnimation.getKeyframes(from, to);

		KeyFrame firstKeyframe = new KeyFrame(new Rectangle());
		KeyFrame secondKeyframe = new KeyFrame(new Rectangle());

		for (int i = 0; i < keyframes.size() - 1; i++) {
			firstKeyframe = keyframes.get(i);
			secondKeyframe = keyframes.get(i + 1);

			if (percentage >= firstKeyframe.getPercentage() && percentage <= secondKeyframe.getPercentage()) {
				break;
			}
		}

		var percentageBetweenKeyframes = (percentage - firstKeyframe.getPercentage())
				/ (secondKeyframe.getPercentage() - firstKeyframe.getPercentage());
		return calculateProgress(from, to, percentageBetweenKeyframes);
	}

	private static Rectangle calculateProgress(Rectangle fromBounds, Rectangle toBounds, double progress) {
		Rectangle bounds = new Rectangle();
		if (fromBounds != null && toBounds != null) {
			bounds.setLocation(calculateProgress(fromBounds.getLocation(), toBounds.getLocation(), progress));
			bounds.setSize(calculateProgress(fromBounds.getSize(), toBounds.getSize(), progress));
		}
		return bounds;
	}

	private static Point calculateProgress(Point fromPoint, Point toPoint, double progress) {
		Point point = new Point();
		if (fromPoint != null && toPoint != null) {
			point.x = calculateProgress(fromPoint.x, toPoint.x, progress);
			point.y = calculateProgress(fromPoint.y, toPoint.y, progress);
		}
		return point;
	}

	private static Dimension calculateProgress(Dimension fromSize, Dimension toSize, double progress) {
		Dimension size = new Dimension();
		if (fromSize != null && toSize != null) {
			size.width = calculateProgress(fromSize.width, toSize.width, progress);
			size.height = calculateProgress(fromSize.height, toSize.height, progress);
		}
		return size;
	}

	private static int calculateProgress(int fromValue, int toValue, double fraction) {
		int value = 0;
		int distance = toValue - fromValue;
		value = (int) Math.round((double) distance * fraction);
		value += fromValue;

		return value;
	}
}
