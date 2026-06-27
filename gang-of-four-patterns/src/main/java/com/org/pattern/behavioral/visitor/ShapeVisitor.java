package com.org.pattern.behavioral.visitor;

/**
 * Visitor — lets you add further operations to objects without modifying them.
 *
 * Real-world analogy: Computing area, perimeter, or export format for shapes
 * without polluting the shape classes with new methods.
 */
public interface ShapeVisitor {
    void visit(Circle circle);
    void visit(Rectangle rectangle);
    void visit(Triangle triangle);
}
