package com.org.pattern.structural.composite.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring Boot — Composite Pattern
 *
 * Spring's Environment / PropertySource hierarchy IS the Composite pattern.
 * - Component    : PropertySource<T>
 * - Leaf         : MapPropertySource, SystemEnvironmentPropertySource,
 *                  PropertiesPropertySource, YamlPropertySourceLoader
 * - Composite    : CompositePropertySource — contains and delegates to child sources
 *
 * Spring Boot merges properties from many sources in priority order:
 *   1. Command-line args
 *   2. System environment variables
 *   3. application-{profile}.yml
 *   4. application.yml
 *   5. @PropertySource files
 *   6. Default values
 *
 * The Environment treats ALL of them as one unified source — classic Composite.
 *
 * Spring Cloud Config adds more leaf sources (fetched from a config server)
 * and injects them into the same composite hierarchy at startup.
 */

// ── Simplified replication of how Spring composes property sources ────────────
class LeafPropertySource extends PropertySource<Map<String, Object>> {

    public LeafPropertySource(String name, Map<String, Object> source) {
        super(name, source);
    }

    @Override
    public Object getProperty(String name) {
        return getSource().get(name);
    }
}

// ── Configuration showing CompositePropertySource ────────────────────────────
@Configuration
class PropertyConfig {

    @Bean
    public CompositePropertySource compositeSource() {
        CompositePropertySource composite = new CompositePropertySource("app-config");

        // Leaf 1 — defaults baked into the app
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("app.timeout", "30s");
        defaults.put("app.retries", "3");
        composite.addPropertySource(new LeafPropertySource("defaults", defaults));

        // Leaf 2 — environment-specific overrides (higher priority, added last)
        Map<String, Object> envOverrides = new HashMap<>();
        envOverrides.put("app.timeout", "10s"); // overrides the default
        composite.addPropertySource(new LeafPropertySource("env-overrides", envOverrides));

        return composite;
    }
}

public class SpringCompositePropertySource {

    public static void demo() {
        System.out.println("=== Spring Composite Pattern — PropertySource Demo ===");

        CompositePropertySource composite = new CompositePropertySource("demo");

        Map<String, Object> defaults = Map.of("app.timeout", "30s", "app.retries", "3");
        Map<String, Object> overrides = Map.of("app.timeout", "10s");

        composite.addPropertySource(new LeafPropertySource("defaults", defaults));
        composite.addPropertySource(new LeafPropertySource("overrides", overrides));

        // CompositePropertySource checks children in order — first match wins
        System.out.println("app.timeout = " + composite.getProperty("app.timeout")); // 10s (override wins)
        System.out.println("app.retries = " + composite.getProperty("app.retries")); // 3 (from defaults)

        System.out.println("""

            Spring Boot's full property source priority (highest first):
              1. Command-line args            --server.port=9090
              2. SPRING_APPLICATION_JSON      env var JSON blob
              3. System environment vars      SERVER_PORT=9090
              4. application-{profile}.yml   profile-specific overrides
              5. application.yml             base config
              6. @PropertySource files       custom property files
              7. Default values              @Value("${prop:default}")

            Spring Cloud Config inserts a "cloud-config" CompositePropertySource
            at priority #2, merging all remote properties transparently.
            """);
    }
}
