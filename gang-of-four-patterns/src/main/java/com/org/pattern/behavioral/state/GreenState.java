package com.org.pattern.behavioral.state;

public class GreenState implements TrafficLightState {

    @Override
    public void handle(TrafficLight light) {
        System.out.println("GREEN: Go! Switching to YELLOW.");
        light.setState(new YellowState());
    }

    @Override
    public String getColor() { return "GREEN"; }
}
