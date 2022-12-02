package com.bmarket.tradeservice.domain.internal;

import com.bmarket.tradeservice.domain.dto.ResponseImageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.bmarket.tradeservice.domain.exception.ExceptionMessage.ERROR_4XX_TO_FRM;
import static com.bmarket.tradeservice.domain.exception.ExceptionMessage.ERROR_5XX_TO_FRM;

@Component
@Slf4j
public class RequestImageApi {
    @Value("${internal.frm}")
    String frmUrl;

    private WebClient baseUrl() {
        return WebClient.builder()
                .baseUrl(frmUrl)
                .build();
    }

    /**
     * 판매 상품 이미지 저장 요청
     */
    public ResponseImageDto postTradeImages(List<MultipartFile> files) {

        MultipartBodyBuilder builder = makeMultipartBuilder(files);

        return baseUrl()
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new IllegalArgumentException(ERROR_4XX_TO_FRM.getMessage())))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new IllegalArgumentException(ERROR_5XX_TO_FRM.getMessage())))
                .bodyToMono(ResponseImageDto.class)
                .block();
    }

    /**
     * 판매 상품 이미지 삭제 요청
     */
    public ResponseImageDto deleteTradeImages(String imageId) {

        return baseUrl()
                .delete()
                .uri("/{image-id}", imageId)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new IllegalArgumentException(ERROR_4XX_TO_FRM.getMessage())))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new IllegalArgumentException(ERROR_5XX_TO_FRM.getMessage())))
                .bodyToMono(ResponseImageDto.class)
                .block();
    }

    /**
     * 수정 요청 API
     */
    public ResponseImageDto updateTradeImages(String imageId, List<MultipartFile> files) {

        MultipartBodyBuilder builder = makeMultipartBuilder(files);

        return baseUrl()
                .put()
                .uri("/{image-id}",imageId)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new IllegalArgumentException(ERROR_4XX_TO_FRM.getMessage())))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new IllegalArgumentException(ERROR_5XX_TO_FRM.getMessage())))
                .bodyToMono(ResponseImageDto.class)
                .block();
}

    /**
     * MultipartBodyBuilder 생성
     */
    private MultipartBodyBuilder makeMultipartBuilder(List<MultipartFile> files) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        files.forEach(m -> builder.part("images", m.getResource()));
        return builder;
    }

}
