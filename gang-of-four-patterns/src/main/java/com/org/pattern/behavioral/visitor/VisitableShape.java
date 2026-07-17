package com.org.pattern.behavioral.visitor;

interface VisitableShape {
    void accept(ShapeVisitor visitor);
}
