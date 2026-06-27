package com.org.pattern.behavioral.observer;

import java.util.ArrayList;
import java.util.List;

public class StockMarket {

    private final String symbol;
    private double price;
    private final List<StockObserver> observers = new ArrayList<>();

    public StockMarket(String symbol, double initialPrice) {
        this.symbol = symbol;
        this.price = initialPrice;
    }

    public void subscribe(StockObserver observer) {
        observers.add(observer);
    }

    public void unsubscribe(StockObserver observer) {
        observers.remove(observer);
    }

    public void setPrice(double newPrice) {
        double change = newPrice - price;
        this.price = newPrice;
        notifyObservers(change);
    }

    private void notifyObservers(double change) {
        observers.forEach(o -> o.onPriceChanged(symbol, price, change));
    }
}
