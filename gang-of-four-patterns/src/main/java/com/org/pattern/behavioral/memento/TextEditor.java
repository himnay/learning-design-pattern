package com.org.pattern.behavioral.memento;

public class TextEditor {

    private String content = "";
    private int cursorPosition = 0;

    public void type(String text) {
        content = content.substring(0, cursorPosition) + text + content.substring(cursorPosition);
        cursorPosition += text.length();
    }

    public EditorMemento save() {
        return new EditorMemento(content, cursorPosition);
    }

    public void restore(EditorMemento memento) {
        this.content = memento.getContent();
        this.cursorPosition = memento.getCursorPosition();
    }

    public String getContent() { return content; }

    @Override
    public String toString() {
        return "Editor{content='" + content + "', cursor=" + cursorPosition + "}";
    }
}
