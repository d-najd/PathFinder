package com.app.animations;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import com.app.data.Piece;

public class AnimatorNew {
	public static Rectangle ripple(Piece piece) {
		var from = AnimatorHelper.calculateCenter(piece);
		var to = AnimatorHelper.calculateEndPos(piece);

		return ripple(from, to, piece.getAnimationPercentage());
	}

	public static Rectangle ripple(Rectangle from, Rectangle to, double percentage) {
		var keyframes = RippleAnimation.getKeyframes(from, to);
		
		
	}

	private static Rectangle calculateProgress(Rectangle startBounds, Rectangle targetBounds, double progress) {
		Rectangle bounds = new Rectangle();
		if (startBounds != null && targetBounds != null) {
			bounds.setLocation(calculateProgress(startBounds.getLocation(), targetBounds.getLocation(), progress));
			bounds.setSize(calculateProgress(startBounds.getSize(), targetBounds.getSize(), progress));
		}
		return bounds;
	}

	private static Point calculateProgress(Point startPoint, Point targetPoint, double progress) {
		Point point = new Point();
		if (startPoint != null && targetPoint != null) {
			point.x = calculateProgress(startPoint.x, targetPoint.x, progress);
			point.y = calculateProgress(startPoint.y, targetPoint.y, progress);
		}
		return point;
	}

	private static Dimension calculateProgress(Dimension startSize, Dimension targetSize, double progress) {
		Dimension size = new Dimension();
		if (startSize != null && targetSize != null) {
			size.width = calculateProgress(startSize.width, targetSize.width, progress);
			size.height = calculateProgress(startSize.height, targetSize.height, progress);
		}
		return size;
	}

	private static int calculateProgress(int startValue, int endValue, double fraction) {
		int value = 0;
		int distance = endValue - startValue;
		value = (int) Math.round((double) distance * fraction);
		value += startValue;

		return value;
	}
}
