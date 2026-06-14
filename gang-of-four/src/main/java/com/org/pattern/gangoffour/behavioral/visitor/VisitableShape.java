package com.org.pattern.gangoffour.behavioral.visitor;

public interface VisitableShape {
    void accept(ShapeVisitor visitor);
}
