package com.org.pattern.behavioral.memento;

import java.util.ArrayDeque;
import java.util.Deque;

public class EditorHistory {

    private final Deque<EditorMemento> history = new ArrayDeque<>();

    public void push(EditorMemento memento) {
        history.push(memento);
    }

    public EditorMemento pop() {
        return history.isEmpty() ? null : history.pop();
    }

    public static void demo() {
        System.out.println("=== Memento Pattern Demo ===");
        TextEditor editor = new TextEditor();
        EditorHistory history = new EditorHistory();

        editor.type("Hello");
        history.push(editor.save());
        System.out.println("After 'Hello': " + editor);

        editor.type(", World");
        history.push(editor.save());
        System.out.println("After ', World': " + editor);

        editor.type("!!!");
        System.out.println("After '!!!': " + editor);

        System.out.println("Undo -> ");
        editor.restore(history.pop());
        System.out.println(editor);

        System.out.println("Undo -> ");
        editor.restore(history.pop());
        System.out.println(editor);
    }
}
