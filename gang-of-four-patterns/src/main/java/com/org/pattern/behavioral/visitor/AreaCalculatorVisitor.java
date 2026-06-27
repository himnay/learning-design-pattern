package com.org.pattern.behavioral.visitor;

public class AreaCalculatorVisitor implements ShapeVisitor {

    @Override
    public void visit(Circle circle) {
        double area = Math.PI * circle.getRadius() * circle.getRadius();
        System.out.printf("Circle area (r=%.1f): %.2f%n", circle.getRadius(), area);
    }

    @Override
    public void visit(Rectangle rectangle) {
        double area = rectangle.getWidth() * rectangle.getHeight();
        System.out.printf("Rectangle area (%.1f x %.1f): %.2f%n",
                rectangle.getWidth(), rectangle.getHeight(), area);
    }

    @Override
    public void visit(Triangle triangle) {
        double area = 0.5 * triangle.getBase() * triangle.getHeight();
        System.out.printf("Triangle area (base=%.1f, h=%.1f): %.2f%n",
                triangle.getBase(), triangle.getHeight(), area);
    }
}
