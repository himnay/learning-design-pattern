package com.org.pattern.creational.prototype.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Spring Boot — Prototype Pattern
 *
 * Spring's @Scope("prototype") IS the Prototype pattern.
 * Every call to getBean() (or every injection) returns a FRESH instance —
 * Spring uses the existing bean definition as the prototype and creates a copy.
 *
 * Use cases:
 *   - Stateful beans (shopping cart, request context, user session data)
 *   - Expensive-to-configure objects that need independent state per caller
 *   - Report generators, batch job steps, connection wrappers
 *
 * Gotcha: injecting a prototype into a singleton via @Autowired only creates
 * ONE prototype at startup. Use ApplicationContext.getBean() or a Provider<T>
 * to get a fresh instance on each use.
 */

// ── Prototype-scoped bean ─────────────────────────────────────────────────────
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)  // new instance on every getBean()
class ShoppingCart {

    private final long id = System.nanoTime();
    private int itemCount = 0;

    public void addItem(String item) {
        itemCount++;
        System.out.println("Cart[" + id + "] added: " + item + " (total: " + itemCount + ")");
    }

    public long getId() { return id; }
}

// ── Configuration-style prototype ────────────────────────────────────────────
@Configuration
class CartConfig {

    @Bean
    @Scope("prototype")
    public ShoppingCart cart() {
        return new ShoppingCart();
    }
}

// ── Correct way: inject ApplicationContext to pull a fresh prototype each time ─
@Service
class CheckoutService {

    private final ApplicationContext context;

    @Autowired
    public CheckoutService(ApplicationContext context) {
        this.context = context;
    }

    public ShoppingCart createCart() {
        // Each call returns a brand new ShoppingCart prototype
        return context.getBean(ShoppingCart.class);
    }
}

public class SpringPrototypeBean {

    public static void demo() {
        System.out.println("=== Spring Prototype Bean Demo ===");
        System.out.println("""
            @Scope("prototype") — Spring returns a NEW instance on every getBean():

              ShoppingCart cart1 = context.getBean(ShoppingCart.class);
              ShoppingCart cart2 = context.getBean(ShoppingCart.class);
              System.out.println(cart1 == cart2); // false — different instances

            Gotcha — @Autowired prototype in a singleton only injects ONCE:
              @Service
              class OrderService {
                  @Autowired ShoppingCart cart; // same cart forever — WRONG
              }

            Fix — use ApplicationContext or javax.inject.Provider<T>:
              @Autowired Provider<ShoppingCart> cartProvider;
              ShoppingCart fresh = cartProvider.get(); // new instance each call
            """);
    }
}
