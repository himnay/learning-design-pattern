package com.org.pattern.behavioral.strategy;

/**
 * Strategy — defines a family of algorithms, encapsulates each one, and makes them interchangeable.
 *
 * Real-world analogy: Choosing a sort algorithm at runtime based on data characteristics.
 */
public interface SortStrategy {
    void sort(int[] array);
    String getName();
}
