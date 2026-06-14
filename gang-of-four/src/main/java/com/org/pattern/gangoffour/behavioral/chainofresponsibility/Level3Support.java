package com.org.pattern.gangoffour.behavioral.chainofresponsibility;

public class Level3Support extends SupportHandler {

    @Override
    public void handle(SupportTicket ticket) {
        System.out.println("L3 Engineering resolved ticket #" + ticket.getId() + ": " + ticket.getIssue());
    }

    public static void demo() {
        System.out.println("=== Chain of Responsibility Pattern Demo ===");
        Level1Support l1 = new Level1Support();
        Level2Support l2 = new Level2Support();
        Level3Support l3 = new Level3Support();
        l1.setNext(l2).setNext(l3);

        l1.handle(new SupportTicket(1, "Password reset", 1));
        l1.handle(new SupportTicket(2, "Account billing issue", 2));
        l1.handle(new SupportTicket(3, "Data corruption in prod", 3));
    }
}
