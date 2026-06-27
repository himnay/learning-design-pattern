package com.org.pattern.behavioral.mediator;

/**
 * Mediator — reduces coupling between components by having them communicate through a central mediator.
 *
 * Real-world analogy: A chat room where users don't talk directly to each other.
 */
public interface ChatMediator {
    void sendMessage(String message, ChatUser sender);
    void addUser(ChatUser user);
}
