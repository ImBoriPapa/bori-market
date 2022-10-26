package com.bmarket.tradeservice.domain.internal;

import com.bmarket.tradeservice.domain.dto.ResponseImageDto;
import com.bmarket.tradeservice.domain.service.TradeCommandService;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class RequestImageApi {

    public ResponseImageDto getImagePath(Long tradeId, List<MultipartFile> files) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("tradeId", tradeId);
        files.stream().forEach(m -> builder.part("images", m.getResource()));

        ResponseImageDto dto = WebClient.create()
                .post()
                .uri("http://localhost:8095/frm/trade")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(ResponseImageDto.class)
                .block();
        return dto;
    }
}
