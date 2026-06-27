package com.org.pattern.structural.facade;

public class Lights {
    public void dim(int level) { System.out.println("Lights: Dimmed to " + level + "%"); }
    public void on() { System.out.println("Lights: ON"); }
}
