package com.org.pattern.behavioral.chainofresponsibility;

/**
 * Chain of Responsibility — passes requests along a chain of handlers until one handles it.
 *
 * Real-world analogy: Customer support tiers — L1, L2, L3 escalation.
 */
public abstract class SupportHandler {

    protected SupportHandler next;

    public SupportHandler setNext(SupportHandler next) {
        this.next = next;
        return next;
    }

    public abstract void handle(SupportTicket ticket);

    protected void escalate(SupportTicket ticket) {
        if (next != null) {
            next.handle(ticket);
        } else {
            System.out.println("Ticket #" + ticket.getId() + " could not be resolved by any handler.");
        }
    }
}
