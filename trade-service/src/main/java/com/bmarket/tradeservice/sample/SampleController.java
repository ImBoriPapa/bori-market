package com.bmarket.tradeservice.sample;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class SampleController {

    @GetMapping("/sample")
    public SampleResponseDto sample() {
        int code = 200;
        String message = "응답 성공";
        List<String> results = List.of("결과1", "결과2", "결과3", "결과4", "결과5");
        log.info("[sample API 동작]");
        log.info("응답 code= {}", code);
        log.info("응답 message= {}", message);
        log.info("응답 results={}", results);정

        return SampleResponseDto.builder()
                .code(200)
                .message(message)
                .results(results)
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SampleResponseDto {
        private Integer code;
        private String message;
        private List<String> results = new ArrayList<>();
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SampleRequestResult {

        private Integer code;
        private String message;
        private List<String> result = new ArrayList<>();


    }

    @GetMapping("/call")
    public void call() {
        SampleRequestResult responseDto = callSample();
        log.info("[call API 동작]");
        log.info("응답 code= {}", responseDto.getCode());
        log.info("응답 message= {}", responseDto.getMessage());
        log.info("응답 result= {}", responseDto.getResult());
        responseDto.getResult().forEach(r -> log.info("응답 results={}", r));
    }


    public SampleRequestResult callSample() {
        log.info("[callSample() 동작]");
        return WebClient.create("http://localhost:8081/sample")
                .get()
                .retrieve()
                .bodyToMono(SampleRequestResult.class)
                .block();
    }
}
