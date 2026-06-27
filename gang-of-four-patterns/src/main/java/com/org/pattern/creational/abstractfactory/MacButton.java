package com.org.pattern.creational.abstractfactory;

public class MacButton implements Button {

    @Override
    public void render() {
        System.out.println("Rendering macOS-style button");
    }

    @Override
    public void onClick() {
        System.out.println("macOS button click event fired");
    }
}
