package com.org.pattern.gangoffour.structural.flyweight;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Forest {

    private final List<Tree> trees = new ArrayList<>();

    public void plantTree(int x, int y, String name, String color, String texture) {
        TreeType type = TreeFactory.getTreeType(name, color, texture);
        trees.add(new Tree(x, y, type));
    }

    public void draw() {
        trees.forEach(Tree::draw);
    }

    public static void demo() {
        System.out.println("=== Flyweight Pattern Demo ===");
        Forest forest = new Forest();
        Random rand = new Random(42);

        for (int i = 0; i < 5; i++) {
            forest.plantTree(rand.nextInt(100), rand.nextInt(100), "Oak", "Green", "rough");
        }
        for (int i = 0; i < 3; i++) {
            forest.plantTree(rand.nextInt(100), rand.nextInt(100), "Pine", "DarkGreen", "smooth");
        }

        forest.draw();
        System.out.println("Unique TreeType objects created: " + TreeFactory.getCachedTypeCount()
                + " (for 8 trees)");
    }
}
