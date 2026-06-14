package com.org.pattern.gangoffour.behavioral.memento;

/**
 * Memento — captures and externalizes an object's internal state so it can be restored later.
 *
 * Real-world analogy: Undo/redo in a text editor.
 */
public class EditorMemento {

    private final String content;
    private final int cursorPosition;

    public EditorMemento(String content, int cursorPosition) {
        this.content = content;
        this.cursorPosition = cursorPosition;
    }

    public String getContent() { return content; }
    public int getCursorPosition() { return cursorPosition; }
}
