package com.org.pattern.gangoffour.structural.bridge;

public class CanvasDrawingAPI implements DrawingAPI {

    @Override
    public void drawCircle(double x, double y, double radius) {
        System.out.printf("Canvas: ctx.arc(%.1f, %.1f, %.1f, 0, 2*Math.PI)%n", x, y, radius);
    }

    @Override
    public void drawRectangle(double x, double y, double width, double height) {
        System.out.printf("Canvas: ctx.fillRect(%.1f, %.1f, %.1f, %.1f)%n", x, y, width, height);
    }

    @Override
    public String getApiName() { return "Canvas"; }
}
