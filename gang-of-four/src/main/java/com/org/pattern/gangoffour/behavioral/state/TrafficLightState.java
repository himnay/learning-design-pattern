package com.org.pattern.gangoffour.behavioral.state;

/**
 * State — allows an object to alter its behavior when its internal state changes.
 *
 * Real-world analogy: A traffic light that changes behavior based on current color.
 */
public interface TrafficLightState {
    void handle(TrafficLight light);
    String getColor();
}
