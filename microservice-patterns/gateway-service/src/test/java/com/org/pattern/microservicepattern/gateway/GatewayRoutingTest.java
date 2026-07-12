package com.org.pattern.microservicepattern.gateway;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.http.Fault;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

/**
 * Drives the gateway end-to-end: real HTTP in, real route matching, real filter chain out.
 * The "upstream" is a WireMock instance so the test owns both success and failure shapes
 * without needing circuit-breaker-service running.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GatewayRoutingTest {

    private static WireMockServer upstream;

    @LocalServerPort
    private int gatewayPort;

    @BeforeAll
    static void startUpstream() {
        upstream = new WireMockServer(0);
        upstream.start();
    }

    @AfterAll
    static void stopUpstream() {
        upstream.stop();
    }

    @BeforeEach
    void resetStubs() {
        // each test owns its own stub slate — no leakage across tests on the shared server
        upstream.resetAll();
    }

    @DynamicPropertySource
    static void routeToWireMock(DynamicPropertyRegistry registry) {
        registry.add("QUOTES_UPSTREAM_URI", () -> "http://localhost:" + upstream.port());
    }

    private WebTestClient client() {
        return WebTestClient.bindToServer().baseUrl("http://localhost:" + gatewayPort).build();
    }

    @Test
    void routesToUpstreamAndRewritesPath() {
        upstream.stubFor(get(urlPathEqualTo("/api/quotes"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"text\":\"Stay hungry, stay foolish.\",\"author\":\"Steve Jobs\"}")));

        client().get().uri("/quotes")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().exists("X-Correlation-Id")
                .expectBody()
                .jsonPath("$.author").isEqualTo("Steve Jobs");
    }

    @Test
    void propagatesClientSuppliedCorrelationId() {
        upstream.stubFor(get(urlPathEqualTo("/api/quotes"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"text\":\"ok\",\"author\":\"a\"}")));

        client().get().uri("/quotes")
                .header("X-Correlation-Id", "test-fixed-id")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("X-Correlation-Id", "test-fixed-id");
    }

    @Test
    void brokenConnectionTripsCircuitBreakerFallback() {
        // WireMock resets the TCP connection instead of responding, simulating a downstream
        // crash — the gateway's CircuitBreaker filter catches the connection error and
        // forwards to the fallback controller instead of surfacing it to the client
        upstream.stubFor(get(urlPathEqualTo("/api/quotes"))
                .willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)));

        client().get().uri("/quotes")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.source").isEqualTo("gateway-fallback");
    }
}
