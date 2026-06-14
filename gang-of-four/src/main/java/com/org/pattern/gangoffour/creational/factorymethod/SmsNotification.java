package com.org.pattern.gangoffour.creational.factorymethod;

public class SmsNotification implements Notification {

    @Override
    public void send(String recipient, String message) {
        System.out.println("SMS to [" + recipient + "]: " + message);
    }

    @Override
    public String getType() {
        return "SMS";
    }
}
