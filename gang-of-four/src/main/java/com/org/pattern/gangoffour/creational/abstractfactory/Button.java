package com.org.pattern.gangoffour.creational.abstractfactory;

/**
 * Abstract Factory — creates families of related objects without specifying concrete classes.
 *
 * Real-world analogy: Cross-platform UI toolkit (Windows vs Mac look-and-feel).
 */
public interface Button {
    void render();
    void onClick();
}
