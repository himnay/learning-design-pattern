package com.org.pattern.gangoffour.behavioral.state;

public class YellowState implements TrafficLightState {

    @Override
    public void handle(TrafficLight light) {
        System.out.println("YELLOW: Caution! Switching to RED.");
        light.setState(new RedState());
    }

    @Override
    public String getColor() { return "YELLOW"; }
}
