package com.org.pattern.creational.singleton.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Spring Boot — Singleton Pattern
 *
 * Spring applies Singleton as its DEFAULT bean scope. Every @Bean, @Component,
 * @Service, @Repository, and @Controller is a singleton within the ApplicationContext
 * unless explicitly scoped otherwise.
 *
 * You never manage the instance — the IoC container does.
 */

// ── Approach 1: @Component (classpath scanning) ──────────────────────────────
@Component
class EmailService {

    public EmailService() {
        System.out.println("EmailService created once by Spring IoC");
    }

    public void send(String to, String body) {
        System.out.println("Email -> " + to + ": " + body);
    }
}

// ── Approach 2: @Bean in @Configuration (explicit factory method) ─────────────
@Configuration
class AppConfig {

    @Bean   // singleton by default — same instance returned on every injection
    public EmailService emailService() {
        return new EmailService();
    }
}

// ── Consumer ─────────────────────────────────────────────────────────────────
@Service
class OrderService {

    private final EmailService emailService; // Spring injects the SAME singleton

    public OrderService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void placeOrder(String userId) {
        emailService.send(userId, "Your order is confirmed.");
    }
}

// ── Demo (illustrates how Spring resolves singletons) ────────────────────────
public class SpringSingletonBean {

    public static void demo() {
        System.out.println("=== Spring Singleton Bean Demo ===");
        System.out.println("""
            In a real Spring Boot app:
              @Autowired EmailService a;
              @Autowired EmailService b;
              System.out.println(a == b); // true — same instance from ApplicationContext

            Spring IoC guarantees one instance per context, thread-safe,
            without any volatile / synchronized / double-check boilerplate.
            """);
    }
}
