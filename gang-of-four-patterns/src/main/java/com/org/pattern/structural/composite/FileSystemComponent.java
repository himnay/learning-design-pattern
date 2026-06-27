package com.org.pattern.structural.composite;

/**
 * Composite — composes objects into tree structures to represent part-whole hierarchies.
 *
 * Real-world analogy: File system with files and directories (a directory can contain files or other directories).
 */
public interface FileSystemComponent {
    String getName();
    long getSize();
    void print(String indent);
}
