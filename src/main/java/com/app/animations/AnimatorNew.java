package com.app.animations;

import java.awt.Rectangle;

import com.app.data.Piece;

public class AnimatorNew {
	public static void ripple(Piece p) {
		var from = AnimatorHelper.calculateCenter(p);
		var to = AnimatorHelper.calculateEndPos(p);

		ripple(from, to, 1);
	}

	public static void ripple(Rectangle from, Rectangle to, double percentage) {
		var keyframes = RippleAnimation.getKeyframes(from, to);
	}
}
