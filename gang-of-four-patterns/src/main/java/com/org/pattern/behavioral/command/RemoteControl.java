package com.org.pattern.behavioral.command;

import java.util.ArrayDeque;
import java.util.Deque;

public class RemoteControl {

    private final Deque<Command> history = new ArrayDeque<>();

    public void press(Command command) {
        command.execute();
        history.push(command);
    }

    public void undoLast() {
        if (!history.isEmpty()) {
            Command last = history.pop();
            System.out.print("Undo -> ");
            last.undo();
        }
    }

    public static void demo() {
        System.out.println("=== Command Pattern Demo ===");
        RemoteControl remote = new RemoteControl();
        Light living = new Light("Living Room");
        Light bedroom = new Light("Bedroom");

        remote.press(new LightOnCommand(living));
        remote.press(new LightOnCommand(bedroom));
        remote.press(new LightOffCommand(living));

        System.out.println("-- Undo --");
        remote.undoLast();
        remote.undoLast();
    }
}
