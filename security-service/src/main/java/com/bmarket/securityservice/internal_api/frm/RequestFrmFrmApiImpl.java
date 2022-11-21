package com.bmarket.securityservice.internal_api.frm;

import com.bmarket.securityservice.exception.custom_exception.internal_api_ex.InternalRequestFailException;
import org.springframework.context.annotation.Profile;
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
public class RequestFrmFrmApiImpl implements RequestFrmApi {
    public static final String GET_DEFAULT_PROFILE_IMAGE_URL = "http://localhost:8095/frm/profile/default";
    public static final String PUT_PROFILE_IMAGE_URL = "http://localhost:8095/frm/profile/";

    /**
     * internal api
     * f.r.m service 로 기본 프로필 이미지 위치 요청
     * 기본 이미지
     */
    @Override
    public String getDefaultProfileImage() {
        return WebClient.create()
                .get()
                .uri(GET_DEFAULT_PROFILE_IMAGE_URL)
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, e -> Mono.error(new InternalRequestFailException(FRM_SERVER_PROBLEM)))
                .bodyToMono(String.class)
                .doOnError(IllegalArgumentException::new)
                .block();
    }

    /**
     * internal api
     * f.r.m service 로 이미지 수정 요청후 이미지 위치 요청
     */
    @Override
    public String requestSaveImage(Long accountId, MultipartFile file) {
        Resource resource = file.getResource();
        return WebClient.create()
                .put()
                .uri(PUT_PROFILE_IMAGE_URL + accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromMultipartData("image", resource))
//                .body(BodyInserters.fromMultipartData("accountId", accountId)
//                        .with("image", resource))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, e -> Mono.error(new InternalRequestFailException(FRM_WRONG_REQUEST)))
                .onStatus(HttpStatus::is5xxServerError, e -> Mono.error(new InternalRequestFailException(FRM_SERVER_PROBLEM)))
                .bodyToMono(String.class)
                .block();
    }
    @Override
    public String requestDeleteImage(Long accountId){
        return WebClient.create()
                .delete()
                .uri(PUT_PROFILE_IMAGE_URL + accountId)
                .retrieve()
                .bodyToMono(String.class).block();
    }
}
