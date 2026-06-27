package com.org.pattern.structural.proxy;

public class RealImage implements Image {

    private final String fileName;

    public RealImage(String fileName) {
        this.fileName = fileName;
        loadFromDisk();
    }

    private void loadFromDisk() {
        System.out.println("RealImage: Loading '" + fileName + "' from disk...");
    }

    @Override
    public void display() {
        System.out.println("RealImage: Displaying '" + fileName + "'");
    }

    @Override
    public String getFileName() {
        return fileName;
    }
}
