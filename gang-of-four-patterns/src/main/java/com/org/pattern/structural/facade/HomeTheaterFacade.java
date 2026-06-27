package com.org.pattern.structural.facade;

public class HomeTheaterFacade {

    private final DVDPlayer dvd;
    private final Projector projector;
    private final SoundSystem sound;
    private final Lights lights;

    public HomeTheaterFacade(DVDPlayer dvd, Projector projector, SoundSystem sound, Lights lights) {
        this.dvd = dvd;
        this.projector = projector;
        this.sound = sound;
        this.lights = lights;
    }

    public void watchMovie(String movie) {
        System.out.println("--- Getting ready to watch: " + movie + " ---");
        lights.dim(10);
        projector.on();
        projector.setWideScreenMode();
        sound.on();
        sound.setSurroundSound();
        sound.setVolume(8);
        dvd.on();
        dvd.play(movie);
    }

    public void endMovie() {
        System.out.println("--- Shutting down theater ---");
        dvd.stop();
        dvd.off();
        sound.off();
        projector.off();
        lights.on();
    }

    public static void demo() {
        System.out.println("=== Facade Pattern Demo ===");
        HomeTheaterFacade theater = new HomeTheaterFacade(
                new DVDPlayer(), new Projector(), new SoundSystem(), new Lights()
        );
        theater.watchMovie("Inception");
        System.out.println();
        theater.endMovie();
    }
}
