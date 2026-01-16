package com.app.Animations;

import java.awt.*;
import java.util.*;
import java.util.List;

public class RippleAnimation {
    private static Animator inputAnim;
    private static double wid;
    private static double hei;

    public static ArrayList<KeyFrame> getKeyFrames(Animator _inputAnim){
        inputAnim = _inputAnim;
        wid = inputAnim.to.getWidth();
        hei = inputAnim.to.getHeight();

        ArrayList<KeyFrame> keyframes = new ArrayList<>();
        keyframes.add(new KeyFrame(0, inputAnim.from));
        keyframes.add(_85());
        //keyframes.add(_75());
        //keyframes.add(_90());
        keyframes.add(new KeyFrame(100, inputAnim.to));
        return keyframes;
    }


    public static KeyFrame _75(){
        double index = 1.1;
        int width = (int) Math.round(wid * index);
        int height = (int) Math.round(hei * index);
        int x = (int) Math.round(inputAnim.to.x);
        int y = (int) Math.round(inputAnim.to.y);
        return new KeyFrame(75, new Rectangle(x, y, width, height));
    }

    public static KeyFrame _85(){
        double index = 1.1;
        int width = (int) Math.round(wid * index);
        int height = (int) Math.round(hei * index);
        //TODO recalculate this since it seems to be wrong value
        int x = (int) Math.round(inputAnim.to.x);
        int y = (int) Math.round(inputAnim.to.y);
        return new KeyFrame(75, new Rectangle(x, y, width, height));
    }


    public static KeyFrame _90(){
        double index = 0.95;
        int x = (int) Math.round(inputAnim.to.x);
        int y = (int) Math.round(inputAnim.to.y);
        int width = (int) Math.round(wid * index);
        int height = (int) Math.round(hei * index);
        return new KeyFrame(90, new Rectangle(x, y, width, height));
    }
}
