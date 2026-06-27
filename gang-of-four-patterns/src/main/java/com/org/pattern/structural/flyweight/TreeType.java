package com.org.pattern.structural.flyweight;

/**
 * Flyweight — uses sharing to efficiently support a large number of fine-grained objects.
 *
 * Real-world analogy: Rendering a forest with thousands of trees — shared intrinsic state (species, texture)
 * is stored once; extrinsic state (position) is passed in at draw time.
 */
public class TreeType {

    private final String name;
    private final String color;
    private final String texture;

    public TreeType(String name, String color, String texture) {
        this.name = name;
        this.color = color;
        this.texture = texture;
        System.out.println("TreeType created: " + name);
    }

    public void draw(int x, int y) {
        System.out.printf("Drawing %s tree [color=%s, texture=%s] at (%d,%d)%n", name, color, texture, x, y);
    }
}
