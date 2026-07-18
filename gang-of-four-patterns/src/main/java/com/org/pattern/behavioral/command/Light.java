package com.org.pattern.behavioral.command;

public class Light {

    private final String location;
    private boolean isOn = false;

    public Light(String location) {
        this.location = location;
    }

    /** Handles turn on. */
    public void turnOn() {
        isOn = true;
        System.out.println(location + " light: ON");
    }

    /** Handles turn off. */
    public void turnOff() {
        isOn = false;
        System.out.println(location + " light: OFF");
    }

    public boolean isOn() { return isOn; }
}
