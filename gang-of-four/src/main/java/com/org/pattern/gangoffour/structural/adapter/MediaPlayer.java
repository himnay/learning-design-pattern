package com.org.pattern.gangoffour.structural.adapter;

/**
 * Adapter — converts an interface into another interface clients expect.
 *
 * Real-world analogy: An audio player that can only play MP3, adapted to also play VLC and MP4.
 */
public interface MediaPlayer {
    void play(String audioType, String fileName);
}
