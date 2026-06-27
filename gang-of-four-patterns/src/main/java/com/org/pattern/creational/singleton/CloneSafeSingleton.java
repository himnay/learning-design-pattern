package com.org.pattern.creational.singleton;

/**
 * Hack 4 — CLONING
 *
 * Problem: If a singleton implements Cloneable (or its superclass does), calling clone()
 * bypasses the constructor and produces a second independent copy — breaking the guarantee.
 *
 * Fix: Override clone() and throw CloneNotSupportedException unconditionally.
 *   This blocks any attempt to clone the singleton, regardless of the Cloneable marker.
 */
public class CloneSafeSingleton implements Cloneable {

    private static CloneSafeSingleton instance;

    private CloneSafeSingleton() {
        if (instance != null) {
            throw new IllegalStateException("Instance already created.");
        }
    }

    public static CloneSafeSingleton getInstance() {
        if (instance == null) {
            synchronized (CloneSafeSingleton.class) {
                if (instance == null) {
                    instance = new CloneSafeSingleton();
                }
            }
        }
        return instance;
    }

    // Override clone() and throw to prevent a second instance via Object.clone()
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cloning a Singleton is not allowed.");
    }

    public static void demo() {
        System.out.println("=== Singleton — Clone Guard Demo ===");
        CloneSafeSingleton original = CloneSafeSingleton.getInstance();
        System.out.println("Original: " + original.hashCode());

        System.out.println("Attempting clone...");
        try {
            CloneSafeSingleton cloned = (CloneSafeSingleton) original.clone();
            System.out.println("Cloned: " + cloned.hashCode() + " (SINGLETON BROKEN!)");
        } catch (CloneNotSupportedException e) {
            System.out.println("Attack blocked: " + e.getMessage());
        }
    }
}
