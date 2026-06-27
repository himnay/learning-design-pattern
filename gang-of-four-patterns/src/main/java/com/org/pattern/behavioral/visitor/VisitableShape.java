package com.org.pattern.behavioral.visitor;

public interface VisitableShape {
    void accept(ShapeVisitor visitor);
}
