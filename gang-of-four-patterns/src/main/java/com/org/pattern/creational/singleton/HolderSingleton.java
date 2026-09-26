package com.org.pattern.creational.singleton;

/**
 * Initialization-on-Demand Holder idiom: the lazy, thread-safe singleton with no {@code volatile}
 * and no locking of its own. {@code Holder} is not initialised until {@link #getInstance()} first
 * touches it, and the JVM runs a class initialiser exactly once, even under contention.
 *
 * <p>Kept separate from {@link ThreadSafeSingleton}: with both idioms in one class there were two
 * code paths creating the same type, so whichever ran second tripped the constructor's
 * "already created" guard (the holder's initialiser then failed with
 * {@code ExceptionInInitializerError}).</p>
 */
public final class HolderSingleton {

    private HolderSingleton() {
        // While Holder is being initialised this reads the default null, so the first construction
        // passes; any later (reflective) call finds the instance and is refused.
        if (Holder.INSTANCE != null) {
            throw new IllegalStateException("Instance already created — reflection attack blocked.");
        }
    }

    public static HolderSingleton getInstance() {
        return Holder.INSTANCE;
    }

    private static final class Holder {
        private static final HolderSingleton INSTANCE = new HolderSingleton();
    }
}
