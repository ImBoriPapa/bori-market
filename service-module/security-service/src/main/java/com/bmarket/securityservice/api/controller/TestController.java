package com.bmarket.securityservice.api.controller;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
public class TestController {

    @GetMapping("/address")
    public void callExternal(){
        Mono<Address[]> mono = WebClient.create()
                .get()
                .uri("http://localhost:8085/address")
                .retrieve()
                .bodyToMono(Address[].class);
        mono.doOnSuccess(m -> {
            Arrays.stream(m).forEach(r -> {
                log.info("city={}", r.getCity());
                log.info("district={}", r.getDistrict());
                log.info("town={}", r.getTown());
            });
        }).subscribe();

    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class TestAddress{
        private String city;
        private String district;
        private String town;
    }

    @GetMapping("/test")
    public ResponseEntity test(){
        log.info("test ok");
        TestDto dto = TestDto.builder()
                .name("보리")
                .age("2")
                .sex("남아").build();
        return ResponseEntity.ok().body(dto);
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestDto{
        private String name;
        private String age;
        private String sex;
    }

    @GetMapping("/jwt-test1")
    public void test1(){
        log.info("test1 ok");
    }

    @GetMapping("/jwt-test2")
    public void test2(){
        log.info("test2 ok");
    }

    @GetMapping("/redirect1")
    public ResponseEntity test3(){


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("/redirect2"));
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).headers(httpHeaders).body("");
    }
    @GetMapping("/redirect2")
    public void test4(){
        log.info("redirect success");
    }
}
