package com.bmarket.securityservice.api.controller;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
public class TestController {



    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class TestAddress {
        private String city;
        private String district;
        private String town;
    }

    @PostMapping("/image-test")
    public void imageTest(@RequestPart MultipartFile image){

        Resource resource = image.getResource();

        Mono<String> body = WebClient.create()
                .post()
                .uri("http://localhost:8095/frm/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromMultipartData("accountId",1L)
                        .with("image",resource)
                )
                .retrieve()
                .bodyToMono(String.class);

        String result = body.block();
        log.info("result={}",result);

    }
//@RequestPart("image")MultipartFile image
    @PostMapping("/test")
    public String test(@RequestPart String test,
                       @RequestPart MultipartFile image
    ) {
        log.info("test={}",test);
        log.info("test ok={}",image.getOriginalFilename());

        return "ok";
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestDto {
        private String name;
        private String age;
        private String sex;
    }

    @GetMapping("/jwt-test1")
    public void test1() {
        log.info("test1 ok");
    }

    @GetMapping("/jwt-test2")
    public void test2() {
        log.info("test2 ok");
    }

    @GetMapping("/redirect1")
    public ResponseEntity test3() {


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("/redirect2"));
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).headers(httpHeaders).body("");
    }

    @GetMapping("/redirect2")
    public void test4() {
        log.info("redirect success");
    }
}
