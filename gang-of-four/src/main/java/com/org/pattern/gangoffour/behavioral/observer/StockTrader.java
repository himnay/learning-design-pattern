package com.org.pattern.gangoffour.behavioral.observer;

public class StockTrader implements StockObserver {

    private final String name;
    private final double alertThreshold;

    public StockTrader(String name, double alertThreshold) {
        this.name = name;
        this.alertThreshold = alertThreshold;
    }

    @Override
    public void onPriceChanged(String symbol, double newPrice, double change) {
        String direction = change >= 0 ? "+" : "";
        System.out.printf("[%s] %s price: $%.2f (%s%.2f)%n", name, symbol, newPrice, direction, change);
        if (Math.abs(change) >= alertThreshold) {
            System.out.printf("  *** ALERT: significant move of %.2f!%n", change);
        }
    }

    public static void demo() {
        System.out.println("=== Observer Pattern Demo ===");
        StockMarket aapl = new StockMarket("AAPL", 180.00);

        StockTrader alice = new StockTrader("Alice", 5.0);
        StockTrader bob = new StockTrader("Bob", 2.0);

        aapl.subscribe(alice);
        aapl.subscribe(bob);

        aapl.setPrice(183.50);
        aapl.setPrice(175.00);

        System.out.println("-- Bob unsubscribes --");
        aapl.unsubscribe(bob);
        aapl.setPrice(190.00);
    }
}
