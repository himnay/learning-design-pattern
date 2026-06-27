package com.org.pattern.creational.abstractfactory;

public class WindowsCheckbox implements Checkbox {

    private boolean checked = false;

    @Override
    public void render() {
        System.out.println("Rendering Windows-style checkbox [" + (checked ? "X" : " ") + "]");
    }

    @Override
    public void toggle() {
        checked = !checked;
        System.out.println("Windows checkbox toggled to: " + checked);
    }
}
