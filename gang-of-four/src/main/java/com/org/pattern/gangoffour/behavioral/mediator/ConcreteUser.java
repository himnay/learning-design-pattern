package com.org.pattern.gangoffour.behavioral.mediator;

public class ConcreteUser extends ChatUser {

    public ConcreteUser(ChatMediator mediator, String name) {
        super(mediator, name);
    }

    @Override
    public void send(String message) {
        System.out.println("[" + name + "] sends: " + message);
        mediator.sendMessage(message, this);
    }

    @Override
    public void receive(String message, String from) {
        System.out.println("[" + name + "] received from [" + from + "]: " + message);
    }
}
