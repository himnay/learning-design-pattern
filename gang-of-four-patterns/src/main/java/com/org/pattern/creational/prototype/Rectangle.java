package com.org.pattern.creational.prototype;

public class Rectangle extends Shape {

    private double width;
    private double height;

    public Rectangle(Rectangle source) {
        super(source);
        if (source != null) {
            this.width = source.width;
            this.height = source.height;
        }
    }

    public Rectangle(double width, double height, String color) {
        super(null);
        this.width = width;
        this.height = height;
        this.color = color;
    }

    @Override
    public Rectangle clone() {
        return new Rectangle(this);
    }

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public String toString() {
        return "Rectangle{width=" + width + ", height=" + height + ", color=" + color + ", area=" + area() + "}";
    }
}
