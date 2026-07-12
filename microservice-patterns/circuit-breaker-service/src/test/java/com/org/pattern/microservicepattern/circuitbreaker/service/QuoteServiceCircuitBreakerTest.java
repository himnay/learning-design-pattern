package com.org.pattern.microservicepattern.circuitbreaker.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.org.pattern.microservicepattern.circuitbreaker.domain.Quote;
import com.org.pattern.microservicepattern.circuitbreaker.downstream.FlakyQuoteClient;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@SpringBootTest
class QuoteServiceCircuitBreakerTest {

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private FlakyQuoteClient flakyQuoteClient;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    private CircuitBreaker breaker;

    @BeforeEach
    void resetBreakerAndDownstream() {
        breaker = circuitBreakerRegistry.circuitBreaker(QuoteService.BACKEND);
        breaker.reset();
        flakyQuoteClient.setFailRatePercent(0);
    }

    @Test
    @DisplayName("healthy downstream: real quote is returned and the breaker stays CLOSED")
    void healthyDownstreamKeepsBreakerClosed() {
        Quote quote = quoteService.getQuote();

        assertThat(quote.source()).isEqualTo(Quote.SOURCE_DOWNSTREAM);
        assertThat(breaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
    }

    @Test
    @DisplayName("forced failures open the breaker and every caller receives the fallback quote")
    void forcedFailuresOpenBreakerAndReturnFallback() {
        flakyQuoteClient.setFailRatePercent(100);

        // Each service call = up to 2 downstream attempts (retry max-attempts: 2), every attempt
        // recorded as a failure by the breaker. minimum-number-of-calls: 5 at 100% failure rate
        // crosses failure-rate-threshold: 50, so the breaker must be OPEN well within 5 calls.
        for (int i = 0; i < 5; i++) {
            Quote quote = quoteService.getQuote();
            assertThat(quote.source())
                    .as("caller %d must be answered by the fallback, never an exception", i)
                    .isEqualTo(Quote.SOURCE_FALLBACK);
        }

        assertThat(breaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
    }

    @Test
    @DisplayName("an OPEN breaker short-circuits: fallback is served without touching the downstream")
    void openBreakerShortCircuitsWithoutCallingDownstream() {
        flakyQuoteClient.setFailRatePercent(100);
        breaker.transitionToOpenState();

        long invocationsBefore = flakyQuoteClient.invocationCount();
        Quote quote = quoteService.getQuote();

        assertThat(quote.source()).isEqualTo(Quote.SOURCE_FALLBACK);
        assertThat(flakyQuoteClient.invocationCount())
                .as("OPEN breaker must not let the call reach the downstream")
                .isEqualTo(invocationsBefore);
        assertThat(breaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
    }
}
