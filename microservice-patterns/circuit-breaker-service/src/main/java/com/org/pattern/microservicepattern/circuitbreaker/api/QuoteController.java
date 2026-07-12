package com.org.pattern.microservicepattern.circuitbreaker.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.pattern.microservicepattern.circuitbreaker.domain.Quote;
import com.org.pattern.microservicepattern.circuitbreaker.service.QuoteService;

@RestController
public class QuoteController {

    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping("/api/quotes")
    public Quote getQuote() {
        return quoteService.getQuote();
    }
}
