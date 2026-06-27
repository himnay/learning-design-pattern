package com.org.pattern.behavioral.chainofresponsibility.spring;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Spring Boot — Chain of Responsibility Pattern
 *
 * Spring Security's filter chain IS the Chain of Responsibility pattern.
 * Each filter handles one specific concern and calls chain.doFilter() to
 * pass the request to the next handler — or short-circuits if it rejects it.
 *
 * Spring Security default filter order (simplified):
 *   SecurityContextPersistenceFilter   → load/save SecurityContext from session
 *   UsernamePasswordAuthenticationFilter → handle form login
 *   BearerTokenAuthenticationFilter    → validate JWT Bearer tokens
 *   BasicAuthenticationFilter          → validate HTTP Basic credentials
 *   ExceptionTranslationFilter         → translate auth/access exceptions to HTTP
 *   FilterSecurityInterceptor          → check method/URL access rules
 *
 * Spring Cloud Gateway uses the same pattern for route filters:
 *   AuthFilter → RateLimitFilter → CircuitBreakerFilter → RouteFilter
 *
 * Servlet @Component filters registered with @Order also form a chain.
 */

// ── Generic request model ─────────────────────────────────────────────────────
record ApiRequest(String path, String authHeader, String body) {}

// ── Abstract handler in our simplified chain ──────────────────────────────────
abstract class SecurityFilter {

    protected SecurityFilter next;

    public SecurityFilter setNext(SecurityFilter next) {
        this.next = next;
        return next;
    }

    public abstract boolean handle(ApiRequest request);

    protected boolean proceed(ApiRequest request) {
        if (next != null) return next.handle(request);
        System.out.println("[Chain] request reached the endpoint handler");
        return true;
    }
}

// ── Filter 1 — Authentication (is the token present and valid?) ───────────────
class AuthenticationFilter extends SecurityFilter {

    @Override
    public boolean handle(ApiRequest request) {
        System.out.println("[AuthFilter] checking token...");
        if (request.authHeader() == null || !request.authHeader().startsWith("Bearer ")) {
            System.out.println("[AuthFilter] REJECTED — missing or invalid token");
            return false;
        }
        System.out.println("[AuthFilter] token OK — passing to next filter");
        return proceed(request);
    }
}

// ── Filter 2 — Rate Limiting (too many requests?) ─────────────────────────────
class RateLimitFilter extends SecurityFilter {

    private int requestCount = 0;
    private static final int LIMIT = 3;

    @Override
    public boolean handle(ApiRequest request) {
        requestCount++;
        System.out.println("[RateLimitFilter] request #" + requestCount);
        if (requestCount > LIMIT) {
            System.out.println("[RateLimitFilter] REJECTED — rate limit exceeded (429)");
            return false;
        }
        return proceed(request);
    }
}

// ── Filter 3 — Logging (audit trail) ─────────────────────────────────────────
class AuditLoggingFilter extends SecurityFilter {

    @Override
    public boolean handle(ApiRequest request) {
        System.out.println("[AuditLog] " + request.path() + " — body length: "
                + (request.body() != null ? request.body().length() : 0));
        return proceed(request);
    }
}

// ── Spring Boot Servlet filter equivalent (what @Component filter looks like) ──
@Component
@Order(1)
class JwtAuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing token");
            return; // short-circuit — do NOT call chain.doFilter()
        }

        // token valid — let the request proceed down the chain
        chain.doFilter(req, res);
    }

    @Override public void init(FilterConfig cfg) {}
    @Override public void destroy() {}
}

public class SpringSecurityFilterChain {

    public static void demo() {
        System.out.println("=== Spring Chain of Responsibility — Security Filter Chain Demo ===");

        AuthenticationFilter auth   = new AuthenticationFilter();
        RateLimitFilter      rate   = new RateLimitFilter();
        AuditLoggingFilter   audit  = new AuditLoggingFilter();

        auth.setNext(rate).setNext(audit);

        System.out.println("-- Request 1: missing token --");
        auth.handle(new ApiRequest("/api/orders", null, null));

        System.out.println("\n-- Request 2: valid token --");
        auth.handle(new ApiRequest("/api/orders", "Bearer eyJhbGc...", "{\"qty\":1}"));

        System.out.println("\n-- Requests 3-5: rate limit hit --");
        for (int i = 0; i < 3; i++) {
            auth.handle(new ApiRequest("/api/orders", "Bearer eyJhbGc...", "{}"));
        }

        System.out.println("""

            Spring Security HttpSecurity configuration (SecurityFilterChain bean):
              @Bean
              public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                  return http
                      .csrf(AbstractHttpConfigurer::disable)
                      .authorizeHttpRequests(auth -> auth
                          .requestMatchers("/public/**").permitAll()
                          .anyRequest().authenticated())
                      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                      .build();
              }

            Spring Cloud Gateway route filters (another chain):
              spring.cloud.gateway.routes:
                - id: orders
                  filters:
                    - AuthenticationFilter
                    - RateLimiter=10,1
                    - CircuitBreaker=inventoryCB
            """);
    }
}
