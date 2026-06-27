package com.org.pattern.structural.bridge;

/**
 * Bridge — decouples abstraction from implementation so both can vary independently.
 *
 * Real-world analogy: Drawing shapes using different rendering APIs (SVG, Canvas).
 */
public interface DrawingAPI {
    void drawCircle(double x, double y, double radius);
    void drawRectangle(double x, double y, double width, double height);
    String getApiName();
}
