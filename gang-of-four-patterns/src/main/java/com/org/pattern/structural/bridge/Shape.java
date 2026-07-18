package com.org.pattern.structural.bridge;

public abstract class Shape {

    protected final DrawingAPI drawingAPI;

    protected Shape(DrawingAPI drawingAPI) {
        this.drawingAPI = drawingAPI;
    }

    /** Handles draw. */
    public abstract void draw();
    /** Handles resize. */
    public abstract void resize(double factor);
}
