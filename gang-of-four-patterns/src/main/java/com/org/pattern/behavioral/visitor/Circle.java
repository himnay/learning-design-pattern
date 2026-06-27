package com.org.pattern.behavioral.visitor;

public class Circle implements VisitableShape {

    private final double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    public double getRadius() { return radius; }

    @Override
    public void accept(ShapeVisitor visitor) {
        visitor.visit(this);
    }
}
