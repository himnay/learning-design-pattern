package com.org.pattern.gangoffour.creational.prototype;

/**
 * Prototype — creates new objects by copying (cloning) an existing object.
 *
 * Real-world analogy: Duplicating graphic design shapes.
 */
public abstract class Shape implements Cloneable {

    protected String color;
    protected int x;
    protected int y;

    public Shape(Shape source) {
        if (source != null) {
            this.color = source.color;
            this.x = source.x;
            this.y = source.y;
        }
    }

    public abstract Shape clone();
    public abstract double area();

    public void setColor(String color) { this.color = color; }
    public void setPosition(int x, int y) { this.x = x; this.y = y; }

    public String getColor() { return color; }
}
