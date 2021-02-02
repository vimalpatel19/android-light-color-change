package com.projects.vimal.lightcolorchange_poc;

/**
 * Created by vimal on 12/8/18.
 */

public class Light {
    private String selector;
    private String power;
    private String color;
    private double duration;
    private boolean fast;

    public Light(String selector) {
        setSelector(selector);
        setPower("on");
        setDuration(0);
        setFast(false);
    }

    public Light(String selector, String color) {
        setSelector(selector);
        setColor(color);
        setPower("on");
        setDuration(0);
        setFast(false);
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public boolean isFast() {
        return fast;
    }

    public void setFast(boolean fast) {
        this.fast = fast;
    }
}
