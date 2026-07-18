package com.org.pattern.behavioral.mediator;

public abstract class ChatUser {

    protected final ChatMediator mediator;
    protected final String name;

    public ChatUser(ChatMediator mediator, String name) {
        this.mediator = mediator;
        this.name = name;
    }

    public String getName() { return name; }

    /** Sends. */
    public abstract void send(String message);
    /** Handles receive. */
    public abstract void receive(String message, String from);
}
