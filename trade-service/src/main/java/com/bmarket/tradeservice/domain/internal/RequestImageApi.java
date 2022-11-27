package com.bmarket.tradeservice.domain.internal;

import com.bmarket.tradeservice.domain.dto.ResponseImageDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class RequestImageApi {

    @Value("${internal.frm}")
    String frmUrl;


    public ResponseImageDto getImagePath(Long tradeId, List<MultipartFile> files) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("tradeId", tradeId);
        files.forEach(m -> builder.part("images", m.getResource()));

        return WebClient.create(frmUrl)
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(ResponseImageDto.class)
                .block();
    }

    // TODO: 2022/11/26 이미지 수정,삭제 요청 구현

    public void deleteImage(Long tradeId){
        WebClient.create(frmUrl)
                .delete()
                .uri("/{tradeId}", tradeId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
