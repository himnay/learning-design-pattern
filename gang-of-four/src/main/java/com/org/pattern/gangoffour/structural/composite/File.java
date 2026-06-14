package com.org.pattern.gangoffour.structural.composite;

public class File implements FileSystemComponent {

    private final String name;
    private final long size;

    public File(String name, long sizeBytes) {
        this.name = name;
        this.size = sizeBytes;
    }

    @Override
    public String getName() { return name; }

    @Override
    public long getSize() { return size; }

    @Override
    public void print(String indent) {
        System.out.println(indent + "- " + name + " (" + size + " bytes)");
    }
}
