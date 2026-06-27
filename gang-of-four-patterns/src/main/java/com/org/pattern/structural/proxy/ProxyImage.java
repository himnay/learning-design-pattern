package com.org.pattern.structural.proxy;

public class ProxyImage implements Image {

    private final String fileName;
    private RealImage realImage;

    public ProxyImage(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void display() {
        if (realImage == null) {
            realImage = new RealImage(fileName);
        }
        realImage.display();
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    public static void demo() {
        System.out.println("=== Proxy Pattern Demo ===");
        Image image = new ProxyImage("photo.jpg");

        System.out.println("Image object created, but not yet loaded.");
        System.out.println("First display call:");
        image.display();

        System.out.println("Second display call (no disk load):");
        image.display();
    }
}
