package com.org.pattern.microservicepattern.circuitbreaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
class CircuitBreakerServiceApplication {

    /** Application entry point. */
    public static void main(String[] args) {
        SpringApplication.run(CircuitBreakerServiceApplication.class, args);
    }

}
