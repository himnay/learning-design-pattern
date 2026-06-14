package com.org.pattern.gangoffour.creational.factorymethod;

public class EmailNotification implements Notification {

    @Override
    public void send(String recipient, String message) {
        System.out.println("Email to [" + recipient + "]: " + message);
    }

    @Override
    public String getType() {
        return "EMAIL";
    }
}
