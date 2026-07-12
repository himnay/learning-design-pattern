package com.org.pattern.microservicepattern.gateway.fallback;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

/**
 * Target of the route-level CircuitBreaker filter's {@code fallbackUri}
 * ({@code forward:/fallback/quotes}). When the quotes route fails — connection error,
 * timeout, a configured 5xx status, or the breaker being OPEN — the gateway forwards
 * the exchange here instead of surfacing the raw error to the client.
 */
@RestController
public class GatewayFallbackController {

    @GetMapping("/fallback/quotes")
    public Mono<Map<String, String>> quotesFallback() {
        return Mono.just(Map.of(
                "text", "Quotes are temporarily unavailable — please try again shortly.",
                "author", "gateway",
                "source", "gateway-fallback"));
    }
}
