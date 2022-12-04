package com.bmarket.securityservice.internal_api.frm;

import com.bmarket.securityservice.exception.custom_exception.internal_api_ex.InternalRequestFailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.bmarket.securityservice.utils.status.ResponseStatus.*;

@Component
public class RequestFrmApi {
    @Value("${internal.frm-profile}")
    private String baseUrl;

    private WebClient createBaseUrl() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();

    }

    /**
     * 회원 가입시 frm-service 에 프로필 이미지 객체를 생성 후 기본 이미지 경로를 반환 받습니다.
     */
    public ResponseImageForm getProfileImage() {
        return createBaseUrl()
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new IllegalArgumentException("요청에러")))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new IllegalArgumentException("frm-service 에러")))
                .bodyToMono(ResponseImageForm.class)
                .block();
    }

    /**
     * 이미지 수정 요청
     */
    public ResponseImageForm putProfileImage(String imageId, MultipartFile file) {
        Resource resource = file.getResource();
        return createBaseUrl()
                .put()
                .uri("/{imageId}",imageId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromMultipartData("image", resource))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, e -> Mono.error(new InternalRequestFailException(FRM_WRONG_REQUEST)))
                .onStatus(HttpStatus::is5xxServerError, e -> Mono.error(new InternalRequestFailException(FRM_SERVER_PROBLEM)))
                .bodyToMono(ResponseImageForm.class)
                .block();
    }

    /**
     *이미지 삭제 요청
     */
    public ResponseImageForm deleteProfileImage(String imageId) {
        return createBaseUrl()
                .delete()
                .uri("/{imageId}",imageId)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, e -> Mono.error(new InternalRequestFailException(FRM_WRONG_REQUEST)))
                .onStatus(HttpStatus::is5xxServerError, e -> Mono.error(new InternalRequestFailException(FRM_SERVER_PROBLEM)))
                .bodyToMono(ResponseImageForm.class)
                .block();
    }
}
