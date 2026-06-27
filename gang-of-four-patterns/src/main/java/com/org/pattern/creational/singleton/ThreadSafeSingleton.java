package com.org.pattern.creational.singleton;

/**
 * Hack 2 — MULTI-THREADING
 *
 * Problem: Without synchronization, two threads can both pass the null-check simultaneously
 * and each create their own instance — breaking the single-instance guarantee.
 *
 * Fix A — Double-Checked Locking + volatile:
 *   - volatile prevents CPU instruction reordering so the reference is not published
 *     before the constructor finishes.
 *   - The outer null-check avoids acquiring the lock on every call after initialization.
 *   - The inner null-check handles the race where two threads pass the outer check.
 *
 * Fix B — Initialization-on-Demand Holder (shown as inner class):
 *   - JVM class loading guarantees that the Holder inner class is only initialized once,
 *     even under concurrent access — no synchronization needed.
 */
public class ThreadSafeSingleton {

    // volatile — ensures visibility across threads and prevents partial construction
    private static volatile ThreadSafeSingleton instance;

    private ThreadSafeSingleton() {
        if (instance != null) {
            throw new IllegalStateException("Instance already created — reflection attack blocked.");
        }
    }

    // Fix A: Double-Checked Locking
    public static ThreadSafeSingleton getInstance() {
        if (instance == null) {
            synchronized (ThreadSafeSingleton.class) {
                if (instance == null) {
                    instance = new ThreadSafeSingleton();
                }
            }
        }
        return instance;
    }

    // Fix B: Initialization-on-Demand Holder (alternative — no volatile needed)
    public static ThreadSafeSingleton getInstanceViaHolder() {
        return Holder.INSTANCE;
    }

    private static final class Holder {
        private static final ThreadSafeSingleton INSTANCE = new ThreadSafeSingleton();
    }

    public static void demo() throws InterruptedException {
        System.out.println("=== Singleton — Thread-Safety Demo ===");
        Runnable task = () -> {
            ThreadSafeSingleton s = ThreadSafeSingleton.getInstance();
            System.out.println(Thread.currentThread().getName() + " got: " + s.hashCode());
        };

        Thread t1 = new Thread(task, "Thread-1");
        Thread t2 = new Thread(task, "Thread-2");
        Thread t3 = new Thread(task, "Thread-3");

        t1.start(); t2.start(); t3.start();
        t1.join(); t2.join(); t3.join();

        System.out.println("Holder variant: " + ThreadSafeSingleton.getInstanceViaHolder().hashCode());
    }
}
