package com.org.pattern.behavioral.strategy.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Spring Boot — Strategy Pattern
 *
 * Spring uses the Strategy pattern in two complementary ways:
 *
 * 1. @ConditionalOnProperty / @ConditionalOnClass / @Profile — AutoConfiguration
 *    selects which concrete strategy bean to register at startup based on
 *    properties, classpath, or active profile. The consumer never changes.
 *
 * 2. Injecting all implementations of an interface as a List<T> or Map<String,T>
 *    and selecting the right one at runtime — a runtime strategy selector.
 *
 * Real Spring examples:
 *   Authentication strategies  → DaoAuthenticationProvider, JwtAuthProvider, OAuth2
 *   Caching strategies         → ConcurrentMapCache, RedisCache, CaffeineCache
 *   Serialization strategies   → Jackson, Gson, XML based on classpath
 *   Validation strategies      → Bean Validation, custom Validator implementations
 *   Transaction strategies     → JpaTransactionManager, DataSourceTransactionManager
 */

// ── Strategy interface ────────────────────────────────────────────────────────
interface PaymentStrategy {
    String getType();
    String pay(double amount);
}

// ── Concrete strategies ───────────────────────────────────────────────────────
@Component("CARD")
class CardPaymentStrategy implements PaymentStrategy {
    @Override public String getType() { return "CARD"; }
    @Override public String pay(double amount) {
        return String.format("Card charged: $%.2f", amount);
    }
}

@Component("PAYPAL")
class PayPalPaymentStrategy implements PaymentStrategy {
    @Override public String getType() { return "PAYPAL"; }
    @Override public String pay(double amount) {
        return String.format("PayPal transfer: $%.2f", amount);
    }
}

@Component("CRYPTO")
class CryptoPaymentStrategy implements PaymentStrategy {
    @Override public String getType() { return "CRYPTO"; }
    @Override public String pay(double amount) {
        return String.format("Crypto transaction: $%.2f (~0.00042 BTC)", amount);
    }
}

// ── Runtime strategy selector: inject ALL strategies, pick by type ────────────
@Service
class PaymentService {

    private final Map<String, PaymentStrategy> strategies;

    // Spring injects ALL PaymentStrategy beans — Map key = bean name
    @Autowired
    public PaymentService(List<PaymentStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(PaymentStrategy::getType, Function.identity()));
    }

    public String processPayment(String type, double amount) {
        PaymentStrategy strategy = strategies.get(type.toUpperCase());
        if (strategy == null) throw new IllegalArgumentException("Unknown payment type: " + type);
        return strategy.pay(amount);
    }
}

// ── @ConditionalOnProperty — AutoConfig selects strategy at startup ───────────
@Configuration
class CacheStrategyConfig {

    // Active when cache.strategy=redis in application.yml
    @Bean
    @ConditionalOnProperty(name = "cache.strategy", havingValue = "redis")
    public String redisCacheManager() {
        return "RedisCacheManager";  // would be a real RedisCacheManager in production
    }

    // Active when cache.strategy=caffeine (or property absent)
    @Bean
    @ConditionalOnProperty(name = "cache.strategy", havingValue = "caffeine", matchIfMissing = true)
    public String caffeineCacheManager() {
        return "CaffeineCacheManager"; // would be a real CaffeineCacheManager in production
    }
}

public class SpringConditionalStrategy {

    public static void demo() {
        System.out.println("=== Spring Strategy Pattern Demo ===");

        // Simulate Spring injecting all PaymentStrategy beans
        List<PaymentStrategy> allStrategies = List.of(
                new CardPaymentStrategy(),
                new PayPalPaymentStrategy(),
                new CryptoPaymentStrategy()
        );
        PaymentService service = new PaymentService(allStrategies);

        System.out.println(service.processPayment("CARD",   99.99));
        System.out.println(service.processPayment("PAYPAL", 49.50));
        System.out.println(service.processPayment("CRYPTO", 200.00));

        System.out.println("""

            Spring Boot auto-config strategy selection:
              # application.yml
              cache.strategy: redis       → Spring wires RedisCacheManager
              cache.strategy: caffeine    → Spring wires CaffeineCacheManager
              (absent)                    → matchIfMissing=true picks Caffeine

            Spring Security auth strategy chain:
              http.authenticationProvider(jwtProvider)
                  .authenticationProvider(daoProvider)
                  .authenticationProvider(oauth2Provider)
              // Spring tries each provider in order — first one that succeeds wins

            Spring @Profile as strategy selector:
              @Profile("prod")  → wires RealPaymentGateway
              @Profile("local") → wires MockPaymentGateway
            """);
    }
}
