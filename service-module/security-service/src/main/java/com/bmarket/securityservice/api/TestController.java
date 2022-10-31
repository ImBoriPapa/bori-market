package com.bmarket.securityservice.api;


import com.bmarket.securityservice.api.account.service.AccountCommandService;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.*;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.reactive.function.BodyInserters;

import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
public class TestController {

    private final AccountCommandService accountCommandService;


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
    public void imageTest(@RequestPart MultipartFile image) {

        Resource resource = image.getResource();
        Mono<String> body = WebClient.create()
                .post()
                .uri("http://localhost:8095/frm/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromMultipartData("accountId", 1L)
                        .with("image", resource)
                )
                .retrieve()
                .bodyToMono(String.class);


        String result = body.block();
        log.info("result={}", result);

    }

    @GetMapping("/test")
    public ResponseEntity test(@RequestParam Long tradeId) {

        ResponseEntity<Test2Dto> block = WebClient.create("http://localhost:8081/internal/trade/" + tradeId)
                .get()
                .retrieve()
                .onStatus(m -> m.is4xxClientError(), r -> Mono.just(new IllegalArgumentException("Error")))
                .toEntity(Test2Dto.class)
                .block();
        return ResponseEntity.ok().body(block.getBody());
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Test2Dto {
        private Long tradeId;
        private String nickname;
        private String title;
        private String context;
        private String category;
        private String townName;
        private List<String> imagePath;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestList {
        private Integer tradeId;
        private String title;
        private String townName;
        private Integer price;
        private String representativeImage;
        private LocalDateTime createdAt;
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
