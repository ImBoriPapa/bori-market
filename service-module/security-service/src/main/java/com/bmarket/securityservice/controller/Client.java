package com.bmarket.securityservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
@Slf4j
public class Client {
    private static final String uri = "http://localhost:8080/test";
    public void response(){

        Flux<String> stringFlux = WebClient.create()
                .get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(String.class);
        stringFlux.subscribe(s -> log.info(s));
    }
}
