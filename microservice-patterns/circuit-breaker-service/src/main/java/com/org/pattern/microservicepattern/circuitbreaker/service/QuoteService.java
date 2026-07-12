package com.org.pattern.microservicepattern.circuitbreaker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.org.pattern.microservicepattern.circuitbreaker.domain.Quote;
import com.org.pattern.microservicepattern.circuitbreaker.downstream.FlakyQuoteClient;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

/**
 * Wraps the flaky downstream call in a Resilience4j circuit breaker and retry.
 *
 * <p>Aspect order (Resilience4j default): {@code Retry( CircuitBreaker( fetchQuote ) )} —
 * the retry sits outside the breaker, so every retry attempt is recorded by the breaker's
 * sliding window. The fallback lives on the <em>outermost</em> annotation ({@code @Retry});
 * putting it on {@code @CircuitBreaker} would swallow failures before the retry ever saw them.
 * When the breaker is OPEN, {@code CallNotPermittedException} is thrown without touching the
 * downstream, the retry ignores it (see {@code ignoreExceptions} in application.yaml), and the
 * fallback answers immediately.
 */
@Service
public class QuoteService {

    public static final String BACKEND = "quoteService";

    private static final Logger log = LoggerFactory.getLogger(QuoteService.class);

    private final FlakyQuoteClient flakyQuoteClient;

    public QuoteService(FlakyQuoteClient flakyQuoteClient) {
        this.flakyQuoteClient = flakyQuoteClient;
    }

    @Retry(name = BACKEND, fallbackMethod = "quoteFallback")
    @CircuitBreaker(name = BACKEND)
    public Quote getQuote() {
        return flakyQuoteClient.fetchQuote();
    }

    /** Invoked when retries are exhausted or the breaker short-circuits the call. */
    Quote quoteFallback(Throwable cause) {
        log.warn("Serving fallback quote: {}", cause.toString());
        return Quote.fallback();
    }
}
