package com.org.pattern.gangoffour.creational.singleton;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Singleton — ensures only one instance of a class exists in the JVM.
 *
 * Real-world analogy: A single shared database connection pool.
 * Thread-safe via double-checked locking + volatile.
 */
public class DatabaseConnection {

    private static volatile DatabaseConnection instance;
    private final String connectionUrl;
    private static final AtomicInteger queryCount = new AtomicInteger(0);

    private DatabaseConnection(String url) {
        this.connectionUrl = url;
        System.out.println("DatabaseConnection created: " + url);
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection("jdbc:postgresql://localhost:5432/appdb");
                }
            }
        }
        return instance;
    }

    public String executeQuery(String sql) {
        int count = queryCount.incrementAndGet();
        return "[Query #" + count + "] on [" + connectionUrl + "]: " + sql;
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public static void demo() {
        System.out.println("=== Singleton Pattern Demo ===");
        DatabaseConnection conn1 = DatabaseConnection.getInstance();
        DatabaseConnection conn2 = DatabaseConnection.getInstance();

        System.out.println("Same instance? " + (conn1 == conn2));
        System.out.println(conn1.executeQuery("SELECT * FROM users"));
        System.out.println(conn2.executeQuery("SELECT * FROM orders"));
    }
}
