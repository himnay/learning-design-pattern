package com.org.pattern.creational.factorymethod;

public class NotificationFactory {

    public static Notification create(String channel) {
        return switch (channel.toUpperCase()) {
            case "EMAIL" -> new EmailNotification();
            case "SMS"   -> new SmsNotification();
            case "PUSH"  -> new PushNotification();
            default      -> throw new IllegalArgumentException("Unknown channel: " + channel);
        };
    }

    public static void demo() {
        System.out.println("=== Factory Method Pattern Demo ===");
        String[] channels = {"EMAIL", "SMS", "PUSH"};
        for (String channel : channels) {
            Notification n = NotificationFactory.create(channel);
            n.send("user@example.com", "Your order has been shipped!");
        }
    }
}
