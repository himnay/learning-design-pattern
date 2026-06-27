package com.org.pattern.structural.facade;

public class SoundSystem {
    public void on() { System.out.println("Sound System: ON"); }
    public void setVolume(int level) { System.out.println("Sound System: Volume set to " + level); }
    public void setSurroundSound() { System.out.println("Sound System: Surround sound activated"); }
    public void off() { System.out.println("Sound System: OFF"); }
}
