package com.org.pattern.behavioral.command;

/**
 * Command — encapsulates a request as an object, enabling undo/redo, queuing, and logging.
 *
 * Real-world analogy: A smart home remote control for lights.
 */
public interface Command {
    void execute();
    void undo();
}
