package com.app.animations;

import java.awt.*;

public class KeyFrame {
    private Rectangle rectangle;
    private Integer percentage;

    public KeyFrame (Integer percentage, Rectangle rectangle){
        this.percentage = percentage;
        this.rectangle = rectangle;
    }
    public KeyFrame (Rectangle rectangle){
        this.rectangle = rectangle;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }
}
