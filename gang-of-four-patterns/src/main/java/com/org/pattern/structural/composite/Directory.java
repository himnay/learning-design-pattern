package com.org.pattern.structural.composite;

import java.util.ArrayList;
import java.util.List;

public class Directory implements FileSystemComponent {

    private final String name;
    private final List<FileSystemComponent> children = new ArrayList<>();

    public Directory(String name) {
        this.name = name;
    }

    public void add(FileSystemComponent component) {
        children.add(component);
    }

    public void remove(FileSystemComponent component) {
        children.remove(component);
    }

    @Override
    public String getName() { return name; }

    @Override
    public long getSize() {
        return children.stream().mapToLong(FileSystemComponent::getSize).sum();
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "+ " + name + "/ (" + getSize() + " bytes)");
        children.forEach(c -> c.print(indent + "  "));
    }

    public static void demo() {
        System.out.println("=== Composite Pattern Demo ===");
        Directory root = new Directory("root");
        Directory src = new Directory("src");
        Directory test = new Directory("test");

        src.add(new File("Main.java", 2048));
        src.add(new File("Config.java", 512));
        test.add(new File("MainTest.java", 1024));

        root.add(src);
        root.add(test);
        root.add(new File("pom.xml", 4096));

        root.print("");
        System.out.println("Total size: " + root.getSize() + " bytes");
    }
}
