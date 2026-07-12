package com.org.pattern.microservicepattern.circuitbreaker.domain;

/**
 * A quote as served by {@code /api/quotes}. The {@code source} field makes it obvious
 * whether the response came from the (flaky) downstream or from the circuit-breaker fallback.
 */
public record Quote(String text, String author, String source) {

    public static final String SOURCE_DOWNSTREAM = "downstream";
    public static final String SOURCE_FALLBACK = "fallback";

    public static Quote downstream(String text, String author) {
        return new Quote(text, author, SOURCE_DOWNSTREAM);
    }

    public static Quote fallback() {
        return new Quote(
                "The best way out is always through. (cached fallback quote)",
                "Robert Frost",
                SOURCE_FALLBACK);
    }
}
