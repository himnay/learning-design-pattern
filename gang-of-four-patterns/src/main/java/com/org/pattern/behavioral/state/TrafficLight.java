package com.org.pattern.behavioral.state;

public class TrafficLight {

    private TrafficLightState state;

    public TrafficLight() {
        this.state = new RedState();
    }

    public void setState(TrafficLightState state) {
        this.state = state;
    }

    public void change() {
        state.handle(this);
    }

    public String getCurrentColor() {
        return state.getColor();
    }

    public static void demo() {
        System.out.println("=== State Pattern Demo ===");
        TrafficLight light = new TrafficLight();
        System.out.println("Initial: " + light.getCurrentColor());

        for (int i = 0; i < 6; i++) {
            light.change();
        }
    }
}
