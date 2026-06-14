package com.org.pattern.gangoffour.creational.prototype;

import java.util.HashMap;
import java.util.Map;

public class ShapeRegistry {

    private final Map<String, Shape> cache = new HashMap<>();

    public void register(String key, Shape shape) {
        cache.put(key, shape);
    }

    public Shape get(String key) {
        Shape prototype = cache.get(key);
        if (prototype == null) throw new IllegalArgumentException("No prototype for key: " + key);
        return prototype.clone();
    }

    public static void demo() {
        System.out.println("=== Prototype Pattern Demo ===");
        ShapeRegistry registry = new ShapeRegistry();
        registry.register("red-circle", new Circle(5.0, "Red"));
        registry.register("blue-rectangle", new Rectangle(4.0, 6.0, "Blue"));

        Shape c1 = registry.get("red-circle");
        Shape c2 = registry.get("red-circle");
        c2.setColor("Green");

        System.out.println("Original: " + c1);
        System.out.println("Clone (modified): " + c2);
        System.out.println("Same instance? " + (c1 == c2));

        Shape r1 = registry.get("blue-rectangle");
        System.out.println("Rectangle clone: " + r1);
    }
}
