package com.org.pattern.gangoffour.creational.factorymethod;

public class PushNotification implements Notification {

    @Override
    public void send(String recipient, String message) {
        System.out.println("Push notification to device [" + recipient + "]: " + message);
    }

    @Override
    public String getType() {
        return "PUSH";
    }
}
