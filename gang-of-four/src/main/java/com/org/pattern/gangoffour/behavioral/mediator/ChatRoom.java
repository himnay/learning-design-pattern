package com.org.pattern.gangoffour.behavioral.mediator;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom implements ChatMediator {

    private final List<ChatUser> users = new ArrayList<>();

    @Override
    public void addUser(ChatUser user) {
        users.add(user);
    }

    @Override
    public void sendMessage(String message, ChatUser sender) {
        users.stream()
                .filter(u -> u != sender)
                .forEach(u -> u.receive(message, sender.getName()));
    }

    public static void demo() {
        System.out.println("=== Mediator Pattern Demo ===");
        ChatRoom room = new ChatRoom();

        ChatUser alice = new ConcreteUser(room, "Alice");
        ChatUser bob = new ConcreteUser(room, "Bob");
        ChatUser carol = new ConcreteUser(room, "Carol");

        room.addUser(alice);
        room.addUser(bob);
        room.addUser(carol);

        alice.send("Hello everyone!");
        bob.send("Hi Alice!");
    }
}
