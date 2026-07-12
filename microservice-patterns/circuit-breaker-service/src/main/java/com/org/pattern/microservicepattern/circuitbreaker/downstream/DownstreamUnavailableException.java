package com.org.pattern.microservicepattern.circuitbreaker.downstream;

/**
 * Thrown by {@link FlakyQuoteClient} to simulate a failing remote dependency
 * (connection refused, 5xx, timeout — collapsed into one exception for the demo).
 */
public class DownstreamUnavailableException extends RuntimeException {

    public DownstreamUnavailableException(String message) {
        super(message);
    }
}
