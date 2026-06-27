package com.org.pattern.creational.factorymethod.spring;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Spring Boot — Factory Method Pattern
 *
 * Spring's entire ApplicationContext / BeanFactory IS the Factory Method pattern.
 * - BeanFactory   → the core factory interface (lazy bean creation)
 * - ApplicationContext → extends BeanFactory (eager, event-aware, i18n)
 * - @Configuration + @Bean → user-defined factory methods
 *
 * The @Bean method IS the factory method: it decides which concrete class
 * to instantiate based on conditions (profile, properties, classpath, etc.).
 */

// ── Product interface ─────────────────────────────────────────────────────────
interface NotificationSender {
    void send(String message);
}

// ── Concrete products ─────────────────────────────────────────────────────────
class EmailSender implements NotificationSender {
    @Override public void send(String message) {
        System.out.println("[EMAIL] " + message);
    }
}

class SmsSender implements NotificationSender {
    @Override public void send(String message) {
        System.out.println("[SMS] " + message);
    }
}

// ── Factory via @Configuration + @Bean ───────────────────────────────────────
@Configuration
class NotificationConfig {

    // @Bean method = Factory Method: the caller asks for NotificationSender,
    // this method decides WHICH concrete type to return.
    @Bean
    public NotificationSender notificationSender() {
        String channel = System.getProperty("notification.channel", "email");
        return switch (channel) {
            case "sms"   -> new SmsSender();
            default      -> new EmailSender();
        };
    }
}

// ── Spring BeanFactory as Factory Method ────────────────────────────────────
@Component
class AlertDispatcher {

    // BeanFactory itself is the factory — ask for a bean by type/name
    private final BeanFactory beanFactory;

    @Autowired
    public AlertDispatcher(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void dispatch(String message) {
        // getBean() is the factory method call — Spring decides the concrete type
        NotificationSender sender = beanFactory.getBean(NotificationSender.class);
        sender.send(message);
    }
}

public class SpringBeanFactory {

    public static void demo() {
        System.out.println("=== Spring Factory Method Pattern Demo ===");
        System.out.println("""
            BeanFactory / ApplicationContext IS the Factory Method pattern:
              NotificationSender sender = context.getBean(NotificationSender.class);
              // Spring returns EmailSender or SmsSender based on config — caller never knows which

            @Bean factory methods make the decision:
              @Bean
              public NotificationSender notificationSender() {
                  return channel.equals("sms") ? new SmsSender() : new EmailSender();
              }

            Spring Cloud adds conditional factories:
              @Bean @ConditionalOnProperty(name="feature.x", havingValue="true")
              public FeatureService featureService() { return new FeatureServiceImpl(); }
            """);
    }
}
