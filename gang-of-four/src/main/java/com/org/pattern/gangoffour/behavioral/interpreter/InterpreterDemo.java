package com.org.pattern.gangoffour.behavioral.interpreter;

public class InterpreterDemo {

    public static void demo() {
        System.out.println("=== Interpreter Pattern Demo ===");

        // Rule: user must be (ADMIN or MANAGER) AND have ACTIVE status
        Expression isAdmin   = new TerminalExpression("ADMIN");
        Expression isManager = new TerminalExpression("MANAGER");
        Expression isActive  = new TerminalExpression("ACTIVE");

        Expression hasRole   = new OrExpression(isAdmin, isManager);
        Expression canAccess = new AndExpression(hasRole, isActive);

        String[] contexts = {
            "ADMIN ACTIVE",
            "MANAGER ACTIVE",
            "USER ACTIVE",
            "ADMIN SUSPENDED"
        };

        for (String ctx : contexts) {
            System.out.printf("Context: %-20s -> Access: %s%n", ctx, canAccess.interpret(ctx));
        }
    }
}
