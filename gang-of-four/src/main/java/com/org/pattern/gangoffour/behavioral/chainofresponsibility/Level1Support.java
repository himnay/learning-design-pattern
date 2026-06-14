package com.org.pattern.gangoffour.behavioral.chainofresponsibility;

public class Level1Support extends SupportHandler {

    @Override
    public void handle(SupportTicket ticket) {
        if (ticket.getSeverity() <= 1) {
            System.out.println("L1 Support resolved ticket #" + ticket.getId() + ": " + ticket.getIssue());
        } else {
            System.out.println("L1 escalating ticket #" + ticket.getId() + " (severity=" + ticket.getSeverity() + ")");
            escalate(ticket);
        }
    }
}
