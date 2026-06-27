package com.org.pattern.structural.decorator;

public class VanillaDecorator extends CoffeeDecorator {

    public VanillaDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return decoratedCoffee.getDescription() + ", Vanilla";
    }

    @Override
    public double getCost() {
        return decoratedCoffee.getCost() + 0.50;
    }

    public static void demo() {
        System.out.println("=== Decorator Pattern Demo ===");
        Coffee coffee = new SimpleCoffee();
        System.out.printf("%s -> $%.2f%n", coffee.getDescription(), coffee.getCost());

        coffee = new MilkDecorator(coffee);
        System.out.printf("%s -> $%.2f%n", coffee.getDescription(), coffee.getCost());

        coffee = new SugarDecorator(coffee);
        System.out.printf("%s -> $%.2f%n", coffee.getDescription(), coffee.getCost());

        coffee = new VanillaDecorator(coffee);
        System.out.printf("%s -> $%.2f%n", coffee.getDescription(), coffee.getCost());
    }
}
