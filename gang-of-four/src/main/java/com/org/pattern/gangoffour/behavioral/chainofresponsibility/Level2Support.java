package com.org.pattern.gangoffour.behavioral.chainofresponsibility;

public class Level2Support extends SupportHandler {

    @Override
    public void handle(SupportTicket ticket) {
        if (ticket.getSeverity() <= 2) {
            System.out.println("L2 Support resolved ticket #" + ticket.getId() + ": " + ticket.getIssue());
        } else {
            System.out.println("L2 escalating ticket #" + ticket.getId() + " (severity=" + ticket.getSeverity() + ")");
            escalate(ticket);
        }
    }
}
