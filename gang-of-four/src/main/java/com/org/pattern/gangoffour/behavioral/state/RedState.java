package com.org.pattern.gangoffour.behavioral.state;

public class RedState implements TrafficLightState {

    @Override
    public void handle(TrafficLight light) {
        System.out.println("RED: Stop! Switching to GREEN.");
        light.setState(new GreenState());
    }

    @Override
    public String getColor() { return "RED"; }
}
