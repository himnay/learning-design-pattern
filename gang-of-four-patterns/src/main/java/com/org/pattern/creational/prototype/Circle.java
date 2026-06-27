package com.org.pattern.creational.prototype;

public class Circle extends Shape {

    private double radius;

    public Circle(Circle source) {
        super(source);
        if (source != null) this.radius = source.radius;
    }

    public Circle(double radius, String color) {
        super(null);
        this.radius = radius;
        this.color = color;
    }

    @Override
    public Circle clone() {
        return new Circle(this);
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    public void setRadius(double radius) { this.radius = radius; }

    @Override
    public String toString() {
        return "Circle{radius=" + radius + ", color=" + color + ", area=" + String.format("%.2f", area()) + "}";
    }
}
