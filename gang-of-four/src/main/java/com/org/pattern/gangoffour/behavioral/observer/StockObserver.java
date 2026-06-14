package com.org.pattern.gangoffour.behavioral.observer;

/**
 * Observer — defines a one-to-many dependency so that when one object changes state,
 * all dependents are notified automatically.
 *
 * Real-world analogy: Stock market — traders subscribe to price updates.
 */
public interface StockObserver {
    void onPriceChanged(String symbol, double newPrice, double change);
}
