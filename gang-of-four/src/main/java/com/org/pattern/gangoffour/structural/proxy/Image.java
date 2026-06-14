package com.org.pattern.gangoffour.structural.proxy;

/**
 * Proxy — provides a surrogate or placeholder to control access to another object.
 *
 * Real-world analogy: Lazy-loading large images — the real image is only loaded from disk when actually displayed.
 */
public interface Image {
    void display();
    String getFileName();
}
