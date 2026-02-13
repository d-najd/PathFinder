package com.app.animations;

import java.awt.*;

public class KeyFrame {
	private Rectangle rectangle;
	private Double percentage;

	public KeyFrame(Double percentage, Rectangle rectangle) {
		assert percentage >= 0 && percentage <= 1;

		this.percentage = percentage;
		this.rectangle = rectangle;
	}

	public KeyFrame(Rectangle rectangle) {
		this.rectangle = rectangle;
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		assert percentage >= 0 && percentage <= 1;

		this.percentage = percentage;
	}
}
