package com.org.pattern.behavioral.command;

public class Light {

    private final String location;
    private boolean isOn = false;

    public Light(String location) {
        this.location = location;
    }

    public void turnOn() {
        isOn = true;
        System.out.println(location + " light: ON");
    }

    public void turnOff() {
        isOn = false;
        System.out.println(location + " light: OFF");
    }

    public boolean isOn() { return isOn; }
}
