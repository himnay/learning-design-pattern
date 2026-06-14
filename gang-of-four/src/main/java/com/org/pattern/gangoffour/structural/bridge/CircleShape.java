package com.org.pattern.gangoffour.structural.bridge;

public class CircleShape extends Shape {

    private double x, y, radius;

    public CircleShape(double x, double y, double radius, DrawingAPI api) {
        super(api);
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    @Override
    public void draw() {
        drawingAPI.drawCircle(x, y, radius);
    }

    @Override
    public void resize(double factor) {
        radius *= factor;
    }

    public static void demo() {
        System.out.println("=== Bridge Pattern Demo ===");
        Shape svgCircle = new CircleShape(10, 20, 5, new SvgDrawingAPI());
        Shape canvasCircle = new CircleShape(10, 20, 5, new CanvasDrawingAPI());

        svgCircle.draw();
        canvasCircle.draw();

        svgCircle.resize(2);
        svgCircle.draw();
    }
}
