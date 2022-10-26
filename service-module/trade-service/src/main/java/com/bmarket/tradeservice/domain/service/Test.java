package com.bmarket.tradeservice.domain.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class Test {


    @PostMapping(value = "/test")
    public void test(@RequestParam(name = "tradId") Long id,
                     @RequestPart(name = "images") List<MultipartFile> images) {

        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part("tradeId", id);

        for (MultipartFile image : images) {
            builder.part("images", image.getResource());
        }

        Form form = WebClient.create()
                .post()
                .uri("http://localhost:8095/frm/trade")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(Form.class)
                .block();
        List<String> collect = form.getImagePath().stream().collect(Collectors.toList());
        for (String s : collect) {
            log.info("result ={}",s);
        }

    }
    @Getter
    @NoArgsConstructor
    public static class Form{
        List<String> imagePath;

        public Form(List<String> path) {
            this.imagePath = path;
        }
    }
}
