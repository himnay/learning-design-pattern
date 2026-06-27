package com.org.pattern.creational.singleton;

/**
 * Hack 1 — REFLECTION
 *
 * Problem: Java Reflection API can call a private constructor, creating a second instance.
 *   Constructor[] ctors = Singleton.class.getDeclaredConstructors();
 *   ctors[0].setAccessible(true);
 *   Singleton second = (Singleton) ctors[0].newInstance(); // breaks naive singleton
 *
 * Fix: Guard inside the private constructor — throw if an instance already exists.
 *   Works on first call, throws on any subsequent reflective call.
 */
public class ReflectionSafeSingleton {

    private static ReflectionSafeSingleton instance;

    private ReflectionSafeSingleton() {
        if (instance != null) {
            throw new IllegalStateException("Instance already created — reflection attack blocked.");
        }
    }

    public static synchronized ReflectionSafeSingleton getInstance() {
        if (instance == null) {
            instance = new ReflectionSafeSingleton();
        }
        return instance;
    }

    public static void demo() {
        System.out.println("=== Singleton — Reflection Guard Demo ===");
        ReflectionSafeSingleton s1 = ReflectionSafeSingleton.getInstance();
        System.out.println("Instance 1: " + s1.hashCode());

        System.out.println("Attempting reflection attack...");
        try {
            var ctor = ReflectionSafeSingleton.class.getDeclaredConstructors()[0];
            ctor.setAccessible(true);
            ReflectionSafeSingleton s2 = (ReflectionSafeSingleton) ctor.newInstance();
            System.out.println("Instance 2: " + s2.hashCode() + " (SINGLETON BROKEN!)");
        } catch (Exception e) {
            System.out.println("Attack blocked: " + e.getCause().getMessage());
        }
    }
}
