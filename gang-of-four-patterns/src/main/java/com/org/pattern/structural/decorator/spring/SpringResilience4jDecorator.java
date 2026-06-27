package com.org.pattern.structural.decorator.spring;

/**
 * Spring Boot — Decorator Pattern
 *
 * Resilience4j uses the Decorator pattern to wrap service calls with
 * fault-tolerance behaviours — without modifying the service itself.
 *
 * Each resilience concern (circuit breaker, retry, rate limiter, bulkhead,
 * time limiter) is an independent decorator. They can be stacked in any order.
 *
 * This is also how Java I/O works:
 *   new BufferedReader(new InputStreamReader(new FileInputStream("file.txt")))
 *   → each wrapper adds a behaviour without changing the inner stream
 *
 * Spring annotations (using resilience4j-spring-boot3):
 *   @CircuitBreaker(name="inventory", fallbackMethod="fallback")
 *   @Retry(name="inventory", fallbackMethod="fallback")
 *   @RateLimiter(name="inventory")
 *   @Bulkhead(name="inventory")
 *   @TimeLimiter(name="inventory")
 */

// ── Core service interface ────────────────────────────────────────────────────
interface InventoryService {
    String checkStock(String productId);
}

// ── Real implementation ───────────────────────────────────────────────────────
class RealInventoryService implements InventoryService {
    @Override
    public String checkStock(String productId) {
        System.out.println("  [RealInventoryService] querying stock for: " + productId);
        if (productId.startsWith("FAIL")) throw new RuntimeException("Downstream unavailable");
        return "IN_STOCK:" + productId;
    }
}

// ── Decorator 1 — Retry ───────────────────────────────────────────────────────
class RetryDecorator implements InventoryService {

    private final InventoryService delegate;
    private final int maxAttempts;

    RetryDecorator(InventoryService delegate, int maxAttempts) {
        this.delegate = delegate;
        this.maxAttempts = maxAttempts;
    }

    @Override
    public String checkStock(String productId) {
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                System.out.println("[RetryDecorator] attempt " + attempt);
                return delegate.checkStock(productId);
            } catch (RuntimeException e) {
                if (attempt == maxAttempts) throw e;
                System.out.println("[RetryDecorator] retrying after failure...");
            }
        }
        throw new RuntimeException("All retries exhausted");
    }
}

// ── Decorator 2 — Circuit Breaker ────────────────────────────────────────────
class CircuitBreakerDecorator implements InventoryService {

    private final InventoryService delegate;
    private int failureCount = 0;
    private static final int THRESHOLD = 2;
    private boolean open = false;

    CircuitBreakerDecorator(InventoryService delegate) {
        this.delegate = delegate;
    }

    @Override
    public String checkStock(String productId) {
        if (open) {
            System.out.println("[CircuitBreaker] OPEN — short-circuiting call");
            return "FALLBACK:CIRCUIT_OPEN";
        }
        try {
            String result = delegate.checkStock(productId);
            failureCount = 0;
            return result;
        } catch (RuntimeException e) {
            failureCount++;
            System.out.println("[CircuitBreaker] failure #" + failureCount);
            if (failureCount >= THRESHOLD) {
                open = true;
                System.out.println("[CircuitBreaker] TRIPPED — circuit is now OPEN");
            }
            throw e;
        }
    }
}

// ── Decorator 3 — Rate Limiter ────────────────────────────────────────────────
class RateLimiterDecorator implements InventoryService {

    private final InventoryService delegate;
    private final int maxCallsPerSecond;
    private int callsThisSecond = 0;

    RateLimiterDecorator(InventoryService delegate, int maxCallsPerSecond) {
        this.delegate = delegate;
        this.maxCallsPerSecond = maxCallsPerSecond;
    }

    @Override
    public String checkStock(String productId) {
        if (callsThisSecond >= maxCallsPerSecond) {
            throw new RuntimeException("[RateLimiter] rate limit exceeded");
        }
        callsThisSecond++;
        System.out.println("[RateLimiter] call #" + callsThisSecond + " permitted");
        return delegate.checkStock(productId);
    }
}

public class SpringResilience4jDecorator {

    public static void demo() {
        System.out.println("=== Spring Decorator Pattern — Resilience4j Demo ===");

        // Stack decorators: RateLimiter → CircuitBreaker → Retry → Real service
        // Each layer adds a concern without modifying the next layer
        InventoryService service = new RateLimiterDecorator(
                new CircuitBreakerDecorator(
                        new RetryDecorator(
                                new RealInventoryService(), 2)),
                5);

        System.out.println("-- Normal call --");
        System.out.println("Result: " + service.checkStock("PROD-001"));

        System.out.println("\n-- Failing call (triggers retry + circuit breaker) --");
        try {
            service.checkStock("FAIL-001");
        } catch (RuntimeException e) {
            System.out.println("Final error: " + e.getMessage());
        }

        System.out.println("""

            Spring Boot equivalent using annotations (resilience4j-spring-boot3):
              @CircuitBreaker(name="inventory", fallbackMethod="stockFallback")
              @Retry(name="inventory")
              @RateLimiter(name="inventory")
              public String checkStock(String productId) { ... }

              public String stockFallback(String productId, Exception e) {
                  return "FALLBACK:OUT_OF_STOCK";
              }

            Java I/O uses the same stacking approach:
              new BufferedReader(
                new InputStreamReader(
                  new FileInputStream("data.txt")))
            """);
    }
}
