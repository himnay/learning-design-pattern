package com.org.pattern.gangoffour.behavioral.visitor;

import java.util.List;

public class PerimeterCalculatorVisitor implements ShapeVisitor {

    @Override
    public void visit(Circle circle) {
        double perimeter = 2 * Math.PI * circle.getRadius();
        System.out.printf("Circle perimeter (r=%.1f): %.2f%n", circle.getRadius(), perimeter);
    }

    @Override
    public void visit(Rectangle rectangle) {
        double perimeter = 2 * (rectangle.getWidth() + rectangle.getHeight());
        System.out.printf("Rectangle perimeter: %.2f%n", perimeter);
    }

    @Override
    public void visit(Triangle triangle) {
        double perimeter = triangle.getSideA() + triangle.getSideB() + triangle.getSideC();
        System.out.printf("Triangle perimeter: %.2f%n", perimeter);
    }

    public static void demo() {
        System.out.println("=== Visitor Pattern Demo ===");
        List<VisitableShape> shapes = List.of(
                new Circle(5),
                new Rectangle(4, 6),
                new Triangle(3, 4, 3, 4, 5)
        );

        System.out.println("-- Area --");
        ShapeVisitor areaCalc = new AreaCalculatorVisitor();
        shapes.forEach(s -> s.accept(areaCalc));

        System.out.println("-- Perimeter --");
        ShapeVisitor perimCalc = new PerimeterCalculatorVisitor();
        shapes.forEach(s -> s.accept(perimCalc));
    }
}
