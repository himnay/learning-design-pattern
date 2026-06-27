package com.org.pattern.creational.singleton;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Hack 3 — SERIALIZATION
 *
 * Problem: During deserialization Java bypasses the constructor and creates a fresh object,
 * so the deserialized object is a different instance from the original singleton.
 *
 * Fix: Implement readResolve() in the singleton class.
 *   When Java finishes deserializing, it calls readResolve() — returning the existing
 *   singleton instance instead of the freshly created one. The fresh object is then GC'd.
 */
public final class SerializationSafeSingleton implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static SerializationSafeSingleton instance;

    private SerializationSafeSingleton() {
        if (instance != null) {
            throw new IllegalStateException("Instance already created.");
        }
    }

    public static SerializationSafeSingleton getInstance() {
        if (instance == null) {
            synchronized (SerializationSafeSingleton.class) {
                if (instance == null) {
                    instance = new SerializationSafeSingleton();
                }
            }
        }
        return instance;
    }

    // Without this method, deserialization creates a brand-new instance.
    // readResolve() intercepts and returns the existing singleton.
    @Serial
    protected Object readResolve() {
        return instance;
    }

    public static void demo() throws IOException, ClassNotFoundException {
        System.out.println("=== Singleton — Serialization Guard Demo ===");
        SerializationSafeSingleton original = SerializationSafeSingleton.getInstance();
        System.out.println("Original : " + original.hashCode());

        Path tempFile = Files.createTempFile("singleton", ".ser");
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(tempFile))) {
            out.writeObject(original);
        }

        SerializationSafeSingleton deserialized;
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(tempFile))) {
            deserialized = (SerializationSafeSingleton) in.readObject();
        }
        Files.deleteIfExists(tempFile);

        System.out.println("Deserialized: " + deserialized.hashCode());
        System.out.println("Same instance? " + (original == deserialized));
    }
}
