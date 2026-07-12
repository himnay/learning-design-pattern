package com.org.pattern.microservicepattern.circuitbreaker.downstream;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

import com.org.pattern.microservicepattern.circuitbreaker.domain.Quote;

/**
 * Simulates a flaky remote quote service. In a real system this would be a
 * {@code RestClient}/{@code WebClient} call to another process; here failure is injected
 * on demand via a configurable failure rate (see {@code /api/chaos?failRate=}), so the
 * circuit breaker's behaviour can be demonstrated deterministically.
 */
@Component
public class FlakyQuoteClient {

    private static final List<Quote> QUOTES = List.of(
            Quote.downstream("Simplicity is prerequisite for reliability.", "Edsger W. Dijkstra"),
            Quote.downstream("It is not the strongest of the species that survives, but the most adaptable.", "Charles Darwin"),
            Quote.downstream("Everything fails, all the time.", "Werner Vogels"),
            Quote.downstream("Hope is not a strategy.", "SRE proverb"));

    /** Percentage of calls (0–100) that throw {@link DownstreamUnavailableException}. */
    private final AtomicInteger failRatePercent = new AtomicInteger(0);
    private final AtomicLong invocations = new AtomicLong(0);

    public Quote fetchQuote() {
        invocations.incrementAndGet();
        if (ThreadLocalRandom.current().nextInt(100) < failRatePercent.get()) {
            throw new DownstreamUnavailableException(
                    "Simulated downstream failure (failRate=" + failRatePercent.get() + "%)");
        }
        return QUOTES.get(ThreadLocalRandom.current().nextInt(QUOTES.size()));
    }

    public int getFailRatePercent() {
        return failRatePercent.get();
    }

    public void setFailRatePercent(int percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("failRate must be between 0 and 100, got " + percent);
        }
        failRatePercent.set(percent);
    }

    /** Number of times the downstream was actually invoked (short-circuited calls never reach it). */
    public long invocationCount() {
        return invocations.get();
    }
}
