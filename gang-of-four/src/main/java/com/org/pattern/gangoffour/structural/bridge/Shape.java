package com.org.pattern.gangoffour.structural.bridge;

public abstract class Shape {

    protected final DrawingAPI drawingAPI;

    protected Shape(DrawingAPI drawingAPI) {
        this.drawingAPI = drawingAPI;
    }

    public abstract void draw();
    public abstract void resize(double factor);
}
