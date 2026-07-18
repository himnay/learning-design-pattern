package com.org.pattern.structural.facade;

public class Projector {
    /** Handles the event. */
    public void on() { System.out.println("Projector: ON"); }
    public void setWideScreenMode() { System.out.println("Projector: Wide screen mode set"); }
    /** Handles off. */
    public void off() { System.out.println("Projector: OFF"); }
}
