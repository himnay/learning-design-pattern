package com.org.pattern.gangoffour.creational.abstractfactory;

public class MacCheckbox implements Checkbox {

    private boolean checked = false;

    @Override
    public void render() {
        System.out.println("Rendering macOS-style checkbox [" + (checked ? "✓" : " ") + "]");
    }

    @Override
    public void toggle() {
        checked = !checked;
        System.out.println("macOS checkbox toggled to: " + checked);
    }
}
