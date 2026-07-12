package com.org.pattern.microservicepattern.circuitbreaker.api;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.org.pattern.microservicepattern.circuitbreaker.downstream.FlakyQuoteClient;

/**
 * Chaos knob for the demo: turns the simulated downstream failure rate up and down at runtime.
 *
 * <pre>
 * curl -X POST "http://localhost:8081/api/chaos?failRate=100"   # break the downstream
 * curl -X POST "http://localhost:8081/api/chaos?failRate=0"     # heal the downstream
 * curl        "http://localhost:8081/api/chaos"                 # inspect current state
 * </pre>
 */
@RestController
@RequestMapping("/api/chaos")
public class ChaosController {

    private final FlakyQuoteClient flakyQuoteClient;

    public ChaosController(FlakyQuoteClient flakyQuoteClient) {
        this.flakyQuoteClient = flakyQuoteClient;
    }

    @GetMapping
    public Map<String, Object> currentChaos() {
        return state();
    }

    @PostMapping
    public Map<String, Object> setFailRate(@RequestParam("failRate") int failRate) {
        flakyQuoteClient.setFailRatePercent(failRate);
        return state();
    }

    private Map<String, Object> state() {
        return Map.of(
                "failRate", flakyQuoteClient.getFailRatePercent(),
                "downstreamInvocations", flakyQuoteClient.invocationCount());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> badFailRate(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
}
