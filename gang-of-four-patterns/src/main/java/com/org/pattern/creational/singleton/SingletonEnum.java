package com.org.pattern.creational.singleton;

/**
 * Alternative — ENUM SINGLETON (Joshua Bloch, Effective Java Item 3)
 *
 * The best way to implement a Singleton in Java. The JVM guarantees:
 *   - Only one instance of each enum constant exists (handles multi-threading automatically).
 *   - Reflection cannot create enum instances (getDeclaredConstructors + setAccessible throws).
 *   - Serialization is handled natively — the same instance is always returned.
 *   - Enums cannot be cloned.
 *
 * The only limitation: cannot extend another class (enums implicitly extend java.lang.Enum).
 * Can still implement interfaces.
 */
public enum SingletonEnum {

    INSTANCE;

    private int someValue;
    private String connectionUrl;

    SingletonEnum() {
        this.connectionUrl = "jdbc:postgresql://localhost:5432/appdb";
        System.out.println("SingletonEnum initialized once by JVM.");
    }

    public int getSomeValue() { return someValue; }
    public void setSomeValue(int value) { this.someValue = value; }
    public String getConnectionUrl() { return connectionUrl; }

    public void doWork() {
        System.out.println("SingletonEnum.doWork() — value=" + someValue + ", url=" + connectionUrl);
    }

    public static void demo() {
        System.out.println("=== Singleton — Enum Variant Demo ===");

        SingletonEnum s1 = SingletonEnum.INSTANCE;
        SingletonEnum s2 = SingletonEnum.INSTANCE;

        s1.setSomeValue(42);
        s1.doWork();

        System.out.println("s1 == s2? " + (s1 == s2));
        System.out.println("Value via s2: " + s2.getSomeValue());

        System.out.println("Attempting reflection attack on enum...");
        try {
            var ctor = SingletonEnum.class.getDeclaredConstructors()[0];
            ctor.setAccessible(true);
            ctor.newInstance("INSTANCE");
            System.out.println("Reflection succeeded (SINGLETON BROKEN!)");
        } catch (Exception e) {
            System.out.println("Reflection blocked by JVM: " + e.getClass().getSimpleName());
        }
    }
}
