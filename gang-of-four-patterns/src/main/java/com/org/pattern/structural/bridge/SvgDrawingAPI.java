package com.org.pattern.structural.bridge;

public class SvgDrawingAPI implements DrawingAPI {

    @Override
    public void drawCircle(double x, double y, double radius) {
        System.out.printf("SVG: <circle cx='%.1f' cy='%.1f' r='%.1f'/>%n", x, y, radius);
    }

    @Override
    public void drawRectangle(double x, double y, double width, double height) {
        System.out.printf("SVG: <rect x='%.1f' y='%.1f' width='%.1f' height='%.1f'/>%n", x, y, width, height);
    }

    @Override
    public String getApiName() { return "SVG"; }
}
