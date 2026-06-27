package com.org.pattern.creational.factorymethod;

/**
 * Factory Method — defines an interface for creating objects but lets subclasses decide which class to instantiate.
 */
public interface Notification {
    void send(String recipient, String message);
    String getType();
}
