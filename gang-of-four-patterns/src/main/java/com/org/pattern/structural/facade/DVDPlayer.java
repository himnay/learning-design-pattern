package com.org.pattern.structural.facade;

/**
 * Facade — provides a simplified interface to a complex subsystem.
 *
 * Real-world analogy: A home theater system with many components simplified through one remote.
 */
public class DVDPlayer {
    public void on() { System.out.println("DVD Player: ON"); }
    public void play(String movie) { System.out.println("DVD Player: Playing '" + movie + "'"); }
    public void stop() { System.out.println("DVD Player: Stopped"); }
    public void off() { System.out.println("DVD Player: OFF"); }
}
