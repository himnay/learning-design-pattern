package com.org.pattern.behavioral.observer.spring;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Spring Boot — Observer Pattern
 *
 * Spring's ApplicationEvent / ApplicationListener system IS the Observer pattern.
 * - Subject  : ApplicationEventPublisher (usually injected into services)
 * - Observer : @EventListener methods or ApplicationListener<E> beans
 * - Event    : any class extending ApplicationEvent (or any plain object in Spring 4.2+)
 *
 * Built-in Spring events (subjects that notify automatically):
 *   ContextRefreshedEvent    → ApplicationContext fully initialized
 *   ApplicationReadyEvent    → app ready to serve requests (after all runners)
 *   ContextClosedEvent       → context shutting down
 *   ApplicationStartedEvent  → Spring Boot app started
 *
 * Spring Cloud Bus extends this: events published to a message broker
 * (Kafka/RabbitMQ) are consumed by observer beans on OTHER service instances.
 */

// ── Custom domain event ───────────────────────────────────────────────────────
class OrderPlacedEvent extends ApplicationEvent {

    private final String orderId;
    private final String customerId;

    public OrderPlacedEvent(Object source, String orderId, String customerId) {
        super(source);
        this.orderId = orderId;
        this.customerId = customerId;
    }

    public String getOrderId()    { return orderId; }
    public String getCustomerId() { return customerId; }
}

// ── Subject: publishes events ─────────────────────────────────────────────────
@Service
class OrderService {

    private final ApplicationEventPublisher publisher;

    public OrderService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void placeOrder(String orderId, String customerId) {
        System.out.println("[OrderService] placing order: " + orderId);
        // ... persist the order ...
        publisher.publishEvent(new OrderPlacedEvent(this, orderId, customerId));
    }
}

// ── Observer 1: send email confirmation ──────────────────────────────────────
@Component
class EmailNotificationListener {

    @EventListener
    public void onOrderPlaced(OrderPlacedEvent event) {
        System.out.println("[EmailListener] sending confirmation to customer: "
                + event.getCustomerId() + " for order: " + event.getOrderId());
    }
}

// ── Observer 2: update inventory ─────────────────────────────────────────────
@Component
class InventoryListener {

    @EventListener
    public void onOrderPlaced(OrderPlacedEvent event) {
        System.out.println("[InventoryListener] reserving stock for order: " + event.getOrderId());
    }
}

// ── Observer 3: built-in Spring events ───────────────────────────────────────
@Component
class AppStartupListener {

    @EventListener
    public void onReady(org.springframework.boot.context.event.ApplicationReadyEvent event) {
        System.out.println("[AppStartupListener] app ready — loading caches...");
    }
}

public class SpringApplicationEventObserver {

    public static void demo() {
        System.out.println("=== Spring Observer Pattern — ApplicationEvent Demo ===");
        System.out.println("""
            In a real Spring Boot app:

              // Subject — publishes the event
              @Service class OrderService {
                  @Autowired ApplicationEventPublisher publisher;

                  public void placeOrder(String id) {
                      // ... business logic ...
                      publisher.publishEvent(new OrderPlacedEvent(this, id, customerId));
                  }
              }

              // Observer A — reacts to the event
              @Component class EmailListener {
                  @EventListener
                  public void handle(OrderPlacedEvent e) { sendEmail(e.getCustomerId()); }
              }

              // Observer B — also reacts, independently
              @Component class InventoryListener {
                  @EventListener
                  public void handle(OrderPlacedEvent e) { reserveStock(e.getOrderId()); }
              }

            The publisher has NO knowledge of the listeners.
            Adding a new observer requires ZERO changes to OrderService.

            @Async + @EventListener = non-blocking observers:
              @Async
              @EventListener
              public void handle(OrderPlacedEvent e) { /* runs on thread pool */ }

            Spring Cloud Bus (cross-service observer):
              publisher.publishEvent(new RefreshRemoteApplicationEvent(...));
              // → sent to Kafka/RabbitMQ → consumed by ALL service instances
              // → each instance triggers @RefreshScope bean refresh
            """);
    }
}
