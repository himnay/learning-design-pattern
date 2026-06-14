package com.org.pattern.gangoffour.behavioral.strategy;

public class Sorter {

    private SortStrategy strategy;

    public Sorter(SortStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(SortStrategy strategy) {
        this.strategy = strategy;
    }

    public void sort(int[] array) {
        System.out.println("Using: " + strategy.getName());
        strategy.sort(array);
    }

    public static void demo() {
        System.out.println("=== Strategy Pattern Demo ===");
        int[] data1 = {5, 2, 8, 1, 9, 3};
        int[] data2 = {5, 2, 8, 1, 9, 3};
        int[] data3 = {5, 2, 8, 1, 9, 3};

        Sorter sorter = new Sorter(new BubbleSortStrategy());
        sorter.sort(data1);

        sorter.setStrategy(new QuickSortStrategy());
        sorter.sort(data2);

        sorter.setStrategy(new MergeSortStrategy());
        sorter.sort(data3);
    }
}
