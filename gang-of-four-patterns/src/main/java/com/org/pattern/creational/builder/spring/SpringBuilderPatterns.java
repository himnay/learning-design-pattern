package com.org.pattern.creational.builder.spring;

import org.springframework.web.util.UriComponentsBuilder;

/**
 * Spring Boot — Builder Pattern
 *
 * Spring uses the Builder pattern pervasively to construct complex, immutable
 * configuration objects step by step.
 *
 * Key Spring / Spring Cloud builders:
 *   UriComponentsBuilder      — build URIs safely without string concatenation
 *   WebClient.Builder         — build reactive HTTP clients (Spring WebFlux)
 *   RestClient.Builder        — build synchronous HTTP clients (Spring 6.1+)
 *   RestTemplateBuilder       — build RestTemplate with timeouts, interceptors
 *   AuthenticationManagerBuilder — configure auth in Spring Security
 *   SpringApplicationBuilder  — build hierarchical Spring Boot app contexts
 *   MockMvcRequestBuilders    — build test requests in @WebMvcTest
 *   Lombok @Builder            — auto-generated builder for domain objects
 */
public class SpringBuilderPatterns {

    public static void demo() {
        System.out.println("=== Spring Builder Pattern Demo ===");

        // ── UriComponentsBuilder ─────────────────────────────────────────────
        var uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.example.com")
                .path("/v1/orders/{orderId}")
                .queryParam("status", "ACTIVE")
                .queryParam("page", 0)
                .buildAndExpand("ORD-123")
                .toUri();

        System.out.println("Built URI: " + uri);

        // ── WebClient.Builder (Spring WebFlux) ───────────────────────────────
        System.out.println("""

            WebClient — reactive HTTP client (spring-boot-starter-webflux):
              WebClient client = WebClient.builder()
                  .baseUrl("https://api.example.com")
                  .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                  .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                  .filter(logRequest())
                  .build();

            RestClient — sync HTTP client (Spring 6.1+):
              RestClient restClient = RestClient.builder()
                  .baseUrl("https://api.example.com")
                  .defaultHeader("X-Api-Key", apiKey)
                  .build();

            RestTemplateBuilder:
              RestTemplate template = new RestTemplateBuilder()
                  .rootUri("https://api.example.com")
                  .connectTimeout(Duration.ofSeconds(5))
                  .readTimeout(Duration.ofSeconds(10))
                  .interceptors(new LoggingInterceptor())
                  .build();

            SpringApplicationBuilder (hierarchical contexts):
              new SpringApplicationBuilder()
                  .parent(ParentConfig.class)
                  .child(ChildConfig.class)
                  .run(args);

            AuthenticationManagerBuilder (Spring Security):
              auth.userDetailsService(userDetailsService)
                  .passwordEncoder(new BCryptPasswordEncoder());
            """);
    }
}
