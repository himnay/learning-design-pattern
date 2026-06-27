package com.org.pattern.structural.proxy.spring;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

/**
 * Spring Boot — Proxy Pattern
 *
 * Spring AOP IS the Proxy pattern at the framework level.
 * Every time you annotate a Spring bean with @Transactional, @Async, @Cacheable,
 * @Retryable, or a custom @Aspect, Spring wraps your bean in a dynamic proxy.
 *
 * The proxy intercepts method calls and injects cross-cutting behaviour
 * BEFORE and/or AFTER delegating to the real object — exactly the Proxy pattern.
 *
 * Spring supports two proxy mechanisms:
 *   JDK Dynamic Proxy  → used when the bean implements an interface
 *   CGLIB Proxy        → used when the bean is a concrete class (subclass-based)
 *
 * Real examples:
 *   @Transactional  → proxy opens/commits/rolls back a DB transaction
 *   @Async          → proxy submits method to a thread pool, returns Future
 *   @Cacheable      → proxy checks the cache; calls real method only on miss
 *   @CircuitBreaker → Resilience4j proxy trips the breaker on failures
 *   @RateLimiter    → Resilience4j proxy throttles calls
 *   @Retry          → Resilience4j proxy retries on exception
 */

// ── Service interface (enables JDK dynamic proxy) ────────────────────────────
interface OrderService {
    void placeOrder(String orderId);
    CompletableFuture<String> processAsync(String orderId);
    String findOrder(String orderId);
}

// ── Real implementation ───────────────────────────────────────────────────────
@Service
class OrderServiceImpl implements OrderService {

    // @Transactional — Spring generates a CGLIB/JDK proxy that wraps this method
    // with: begin → execute → commit (or rollback on RuntimeException)
    @Override
    @Transactional
    public void placeOrder(String orderId) {
        System.out.println("Placing order: " + orderId);
        // any RuntimeException here → proxy triggers rollback automatically
    }

    // @Async — proxy submits this to the configured Executor thread pool.
    // Caller gets the CompletableFuture immediately; work runs on another thread.
    @Override
    @Async("taskExecutor")
    public CompletableFuture<String> processAsync(String orderId) {
        System.out.println("Processing " + orderId + " on " + Thread.currentThread().getName());
        return CompletableFuture.completedFuture("PROCESSED:" + orderId);
    }

    // @Cacheable — proxy checks cache "orders" for key=orderId.
    // On hit: returns cached value without entering this method.
    // On miss: executes method, stores result in cache, returns it.
    @Override
    @Cacheable(value = "orders", key = "#orderId")
    public String findOrder(String orderId) {
        System.out.println("Cache MISS — hitting database for: " + orderId);
        return "ORDER-DATA-" + orderId;
    }
}

// ── What Spring generates under the hood (simplified) ────────────────────────
class TransactionalProxy implements OrderService {

    private final OrderServiceImpl real;

    TransactionalProxy(OrderServiceImpl real) {
        this.real = real;
    }

    @Override
    public void placeOrder(String orderId) {
        System.out.println("[Proxy] BEGIN TRANSACTION");
        try {
            real.placeOrder(orderId);             // delegate to real object
            System.out.println("[Proxy] COMMIT");
        } catch (RuntimeException e) {
            System.out.println("[Proxy] ROLLBACK");
            throw e;
        }
    }

    @Override
    public CompletableFuture<String> processAsync(String orderId) {
        return real.processAsync(orderId);
    }

    @Override
    public String findOrder(String orderId) {
        return real.findOrder(orderId);
    }
}

public class SpringAopProxy {

    public static void demo() {
        System.out.println("=== Spring AOP Proxy Pattern Demo ===");

        // Simplified proxy demo (without Spring container)
        OrderServiceImpl real = new OrderServiceImpl();
        OrderService proxy = new TransactionalProxy(real);

        proxy.placeOrder("ORD-001");

        System.out.println("""

            What Spring actually generates at startup:
              @Autowired OrderService service;
              // Spring injects a CGLIB proxy, NOT OrderServiceImpl directly

              // service.placeOrder("ORD-001") call flow:
              //   → proxy intercepts
              //   → proxy opens transaction
              //   → proxy calls real.placeOrder()
              //   → proxy commits (or rolls back)

            Resilience4j adds more proxy layers (decorator chain):
              @CircuitBreaker(name="orders")
              @RateLimiter(name="orders")
              @Retry(name="orders")
              public String callDownstream() { ... }
              // Each annotation wraps the method in another proxy layer
            """);
    }
}
