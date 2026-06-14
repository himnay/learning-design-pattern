package com.org.pattern.gangoffour.creational.abstractfactory;

public class Application {

    private final Button button;
    private final Checkbox checkbox;

    public Application(GUIFactory factory) {
        this.button = factory.createButton();
        this.checkbox = factory.createCheckbox();
    }

    public void render() {
        button.render();
        checkbox.render();
    }

    public static void demo() {
        System.out.println("=== Abstract Factory Pattern Demo ===");
        String os = System.getProperty("os.name", "Windows").toLowerCase();

        GUIFactory factory = os.contains("mac") ? new MacFactory() : new WindowsFactory();
        Application app = new Application(factory);
        app.render();

        System.out.println("-- Forcing Windows factory --");
        new Application(new WindowsFactory()).render();

        System.out.println("-- Forcing Mac factory --");
        new Application(new MacFactory()).render();
    }
}
