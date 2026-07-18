package com.org.pattern.structural.facade;

public class Lights {
    /** Handles dim. */
    public void dim(int level) { System.out.println("Lights: Dimmed to " + level + "%"); }
    /** Handles the event. */
    public void on() { System.out.println("Lights: ON"); }
}
