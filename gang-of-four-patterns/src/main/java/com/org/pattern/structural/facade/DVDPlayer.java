package com.org.pattern.structural.facade;

/**
 * Facade — provides a simplified interface to a complex subsystem.
 *
 * Real-world analogy: A home theater system with many components simplified through one remote.
 */
public class DVDPlayer {
    /** Handles the event. */
    public void on() { System.out.println("DVD Player: ON"); }
    /** Handles play. */
    public void play(String movie) { System.out.println("DVD Player: Playing '" + movie + "'"); }
    /** Stops. */
    public void stop() { System.out.println("DVD Player: Stopped"); }
    /** Handles off. */
    public void off() { System.out.println("DVD Player: OFF"); }
}
