package com.org.pattern.gangoffour.structural.decorator;

/**
 * Decorator — attaches additional responsibilities to an object dynamically.
 *
 * Real-world analogy: Coffee shop drinks — start with a base coffee and wrap with add-ons.
 */
public interface Coffee {
    String getDescription();
    double getCost();
}
