package com.app.animations;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class Animator {
    public int animationLength = 500; //default animation length is 500mil
    public static int animationUpdates = 0; //number of times the animation gets updated, in milliseconds

    public JPanel panel;
    public Rectangle from;
    public Rectangle to;

    private long startTime;
    /**
     * the first value is the percentage at which the next part of the animation will start, the Animator is for the
     * positions n stuff that it will have
     */
    private ArrayList<KeyFrame> keyframes;

    /**
     * animator setup
     * @param panel the panel that is being animated
     * @param from how we want the panel to look at start of the animation
     * @param to how we want the panel to look at the end of the animation
     */
    public Animator(JPanel panel, Rectangle from, Rectangle to) {
        this.panel = panel;
        this.from = from;
        this.to = to;
    }

    /**
     * animator setup
     * @param panel the panel that is being animated
     * @param from how we want the panel to look at start of the animation
     * @param to how we want the panel to look at the end of the animation
     * @param animationLength the length of the animation (in milliseconds)
     */
    public Animator(JPanel panel, Rectangle from, Rectangle to, int animationLength){
        this.panel = panel;
        this.from = from;
        this.to = to;
        this.animationLength = animationLength;
    }

    /**
     * animator setup
     * @param panel the panel that is being animated
     * @param from how we want the panel to look at start of the animation
     * @param to how we want the panel to look at the end of the animation
     * @param animationLength the length of the animation (in milliseconds)
     * @param keyframes the number of keyframes the animation will have
     */

    public Animator(JPanel panel, Rectangle from, Rectangle to, int animationLength, ArrayList<KeyFrame> keyframes){
        this.panel = panel;
        this.from = from;
        this.to = to;
        this.animationLength = animationLength;
        this.keyframes = keyframes;
    }

    public void ripple(){
        keyframes = RippleAnimation.getKeyFrames(this);
        keyframeAnimator();
    }

    public void keyframeAnimator(){
        //basically a foreach loop with waiting for animations to finish in a separate thread
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                Object obj = new Object();
                try {
                    synchronized (obj) {
                        for (int i = 1; i < keyframes.size(); i++){
                            KeyFrame lastKeyframe = keyframes.get(i - 1);
                            KeyFrame curKeyframe = keyframes.get(i);

                            //percentage I am multiplying with .01 and getting the same thing
                            double percentage = (curKeyframe.getPercentage() - lastKeyframe.getPercentage()) * 0.01;
                            //the length of the current keyframe in milliseconds
                            int keyframeLen = (int) Math.round(percentage * animationLength);
                            panel.removeAll();
                            basicAnimator(keyframeLen, lastKeyframe.getRectangle(), curKeyframe.getRectangle());
                            System.out.println("started new anim " + i);

                            obj.wait(keyframeLen);
                        }
                    }
                } catch (InterruptedException ex) {
                    //SomeFishCatching in that old town road
                }
            }
        };
        thread.start();
    }

    /**
     * an example animation with 2 keyframes, from and to
     */
    public void basicAnimator(){
        basicAnimator(null, null, null);
    }

    /**
     * an example animation with 2 keyframes, from and to
     * @param animationLength for passing animation length. if null will use the default value (the one when animator is created)
     * @param from start position and size of the rectangle, if not specified will use default value
     * @param to end position and size of the rectangle, if not specified will use default value
     */
    public void basicAnimator(Integer animationLength, Rectangle from, Rectangle to) {
        //setup
        if (animationLength == null)
            animationLength = this.animationLength;
        if (from == null)
            from = this.from;
        if (to == null)
            to = this.to;
        Integer final_animationLength = animationLength;
        Rectangle finalFrom = from;
        Rectangle finalTo = to;

        //animation
        Timer timer = new Timer(animationUpdates, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long duration = System.currentTimeMillis() - startTime;
                double progress = (double)duration / (double) final_animationLength;
                if (progress > 1f) {
                    progress = 1f;
                    ((Timer)e.getSource()).stop();
                }
                Rectangle target = calculateProgress(finalFrom, finalTo, progress);
                panel.setBounds(target);
            }
        });
        timer.setRepeats(true);
        timer.setCoalesce(true);
        timer.setInitialDelay(0);
        startTime = System.currentTimeMillis();
        timer.start();
    }

    public static Rectangle calculateProgress(Rectangle startBounds, Rectangle targetBounds, double progress) {
        Rectangle bounds = new Rectangle();
        if (startBounds != null && targetBounds != null) {
            bounds.setLocation(calculateProgress(startBounds.getLocation(), targetBounds.getLocation(), progress));
            bounds.setSize(calculateProgress(startBounds.getSize(), targetBounds.getSize(), progress));
        }
        return bounds;
    }

    public static Point calculateProgress(Point startPoint, Point targetPoint, double progress) {
        Point point = new Point();
        if (startPoint != null && targetPoint != null) {
            point.x = calculateProgress(startPoint.x, targetPoint.x, progress);
            point.y = calculateProgress(startPoint.y, targetPoint.y, progress);
        }
        return point;
    }

    public static int calculateProgress(int startValue, int endValue, double fraction) {
        int value = 0;
        int distance = endValue - startValue;
        value = (int)Math.round((double)distance * fraction);
        value += startValue;

        return value;
    }

    public static Dimension calculateProgress(Dimension startSize, Dimension targetSize, double progress) {
        Dimension size = new Dimension();
        if (startSize != null && targetSize != null) {
            size.width = calculateProgress(startSize.width, targetSize.width, progress);
            size.height = calculateProgress(startSize.height, targetSize.height, progress);
        }
        return size;
    }
}

