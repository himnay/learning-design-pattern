package com.org.pattern.creational.builder;

import java.util.List;

/**
 * Builder — constructs complex objects step by step, separating construction from representation.
 *
 * Real-world analogy: Building a customized pizza order.
 */
public class Pizza {

    private final String size;
    private final String crust;
    private final String sauce;
    private final List<String> toppings;
    private final boolean extraCheese;
    private final boolean glutenFree;

    private Pizza(Builder builder) {
        this.size = builder.size;
        this.crust = builder.crust;
        this.sauce = builder.sauce;
        this.toppings = List.copyOf(builder.toppings);
        this.extraCheese = builder.extraCheese;
        this.glutenFree = builder.glutenFree;
    }

    @Override
    public String toString() {
        return "Pizza{size=" + size + ", crust=" + crust + ", sauce=" + sauce
                + ", toppings=" + toppings + ", extraCheese=" + extraCheese
                + ", glutenFree=" + glutenFree + "}";
    }

    public static class Builder {
        private String size = "Medium";
        private String crust = "Thin";
        private String sauce = "Tomato";
        private List<String> toppings = List.of();
        private boolean extraCheese = false;
        private boolean glutenFree = false;

        public Builder size(String size) {
            this.size = size;
            return this;
        }

        public Builder crust(String crust) {
            this.crust = crust;
            return this;
        }

        public Builder sauce(String sauce) {
            this.sauce = sauce;
            return this;
        }

        public Builder toppings(List<String> toppings) {
            this.toppings = toppings;
            return this;
        }

        public Builder extraCheese(boolean extraCheese) {
            this.extraCheese = extraCheese;
            return this;
        }

        public Builder glutenFree(boolean glutenFree) {
            this.glutenFree = glutenFree;
            return this;
        }

        public Pizza build() {
            if (size == null || size.isBlank()) throw new IllegalStateException("Size is required");
            return new Pizza(this);
        }
    }

    public static void demo() {
        System.out.println("=== Builder Pattern Demo ===");
        Pizza margherita = new Pizza.Builder()
                .size("Large")
                .crust("Thin")
                .sauce("Tomato")
                .toppings(List.of("Mozzarella", "Basil"))
                .build();

        Pizza veggie = new Pizza.Builder()
                .size("Medium")
                .crust("Stuffed")
                .sauce("Pesto")
                .toppings(List.of("Bell Peppers", "Mushrooms", "Olives"))
                .extraCheese(true)
                .glutenFree(true)
                .build();

        System.out.println(margherita);
        System.out.println(veggie);
    }
}
