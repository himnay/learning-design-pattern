package com.org.pattern.behavioral.visitor;

public class Triangle implements VisitableShape {

    private final double base;
    private final double height;
    private final double sideA;
    private final double sideB;
    private final double sideC;

    public Triangle(double base, double height, double sideA, double sideB, double sideC) {
        this.base = base;
        this.height = height;
        this.sideA = sideA;
        this.sideB = sideB;
        this.sideC = sideC;
    }

    public double getBase() { return base; }
    public double getHeight() { return height; }
    public double getSideA() { return sideA; }
    public double getSideB() { return sideB; }
    public double getSideC() { return sideC; }

    @Override
    public void accept(ShapeVisitor visitor) {
        visitor.visit(this);
    }
}
