package com.bmarket.securityservice.internal_api.trade;

import com.bmarket.securityservice.domain.address.AddressRange;
import com.bmarket.securityservice.domain.profile.controller.RequestProfileForm;
import com.bmarket.securityservice.domain.trade.controller.resultForm.RequestGetTradeListResult;
import com.bmarket.securityservice.domain.trade.controller.resultForm.ResponseCreateTradeResult;
import com.bmarket.securityservice.internal_api.trade.form.RequestTradeForm;
import com.bmarket.securityservice.internal_api.trade.form.SearchCondition;
import com.bmarket.securityservice.internal_api.trade.form.TradeDetailResult;
import com.bmarket.securityservice.internal_api.trade.form.TradeListResult;
import com.bmarket.securityservice.exception.custom_exception.internal_api_ex.InternalRequestFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static com.bmarket.securityservice.utils.status.ResponseStatus.*;


@Component
@RequiredArgsConstructor
@Slf4j
public class RequestTradeApi {

    @Value("${internal.trade}")
    private String tradeServiceUrl;

    // TODO: 2022/10/31 onStatus 에러 처리
    // TODO: 2022/11/17 거래 상태 변경 로직 추가
    public WebClient baseUrl() {
        return WebClient.builder()
                .baseUrl(tradeServiceUrl)
                .build();
    }

    /**
     * POST "/internal/trade
     * 거래 생성 요청 api
     * RequestTradeForm, List<MultipartFile>
     */
    public ResponseCreateTradeResult requestCreateTrade(RequestTradeForm form, List<MultipartFile> images) {

        MultiValueMap<String, HttpEntity<?>> map = getMultiValueMap(form, images);
        return baseUrl()
                .post()
                .uri("/internal/trade")
                .body(BodyInserters.fromMultipartData(map))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        response -> Mono.error(() -> new InternalRequestFailException(TRADE_WRONG_REQUEST)))
                .onStatus(HttpStatus::is5xxServerError,
                        response -> Mono.error(() -> new InternalRequestFailException(TRADE_SERVER_PROBLEM)))
                .bodyToMono(ResponseCreateTradeResult.class)
                .block();
    }

    /**
     * 멀티파트 리스트 -> MultiValueMap
     */
    private static MultiValueMap<String, HttpEntity<?>> getMultiValueMap(RequestTradeForm form, List<MultipartFile> images) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part("form", form);

        for (MultipartFile image : images) {
            builder.part("images", image.getResource());
        }
        return builder.build();
    }

    /**
     * 계정 아이디로 거래 내역 조회 요청 API
     */
    public List<RequestGetTradeListResult> requestTradeListByAccountId(Long accountId) {
        return baseUrl()
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/internal/trade")
                                .queryParam("accountId", accountId)
                                .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        response -> Mono.error(() -> new InternalRequestFailException(TRADE_WRONG_REQUEST)))
                .onStatus(HttpStatus::is5xxServerError,
                        response -> Mono.error(() -> new InternalRequestFailException(TRADE_SERVER_PROBLEM)))
                .bodyToMono(RequestGetTradeListResult[].class)
                .map(Arrays::asList)
                .block();
    }

    /**
     * 검색 조건에 따른 거래 목록 조회 api
     */
    public TradeListResult requestGetTradeList(SearchCondition condition) {
        return baseUrl()
                .get()
                .uri(UriBuilder -> UriBuilder.path("/internal/trade")
                        .queryParam("size", condition.getSize())
                        .queryParam("lastIndex", condition.getLastIndex())
                        .queryParam("category", condition.getCategory())
                        .queryParam("isShare", condition.getIsShare())
                        .queryParam("isOffer", condition.getIsOffer())
                        .queryParam("status", condition.getStatus())
                        .queryParam("addressCode", condition.getAddressCode())
                        .queryParam("range", condition.getRange())
                        .build()
                )
                .retrieve()
                .bodyToMono(TradeListResult.class)
                .block();
    }

    /**
     * 거래 상세 내역 조회 요청 api
     */
    public TradeDetailResult requestGetTrade(Long tradeId) {
        return baseUrl()
                .get()
                .uri("/internal/trade/{tradeId}", tradeId)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, r -> Mono.just(new InternalRequestFailException(TRADE_WRONG_REQUEST)))
                .onStatus(HttpStatus::is5xxServerError, r -> Mono.just(new InternalRequestFailException(TRADE_SERVER_PROBLEM)))
                .bodyToMono(TradeDetailResult.class)
                .block();
    }

    /**
     * 닉네임 변경 요청 API
     */
    public String requestUpdateNickname(Long accountId, RequestProfileForm.UpdateNickname form) {
       return baseUrl()
                .patch()
                .uri("/internal/trade/account/{accountId}/nickname", accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(form)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    /**
     * 프로필 이미지 변경 요청 API
     */
    public void requestUpdateProfileImage(Long accountId, String profileImage) {
        baseUrl()
                .patch()
                .uri(uriBuilder ->
                        uriBuilder.path("/internal/trade")
                                .queryParam("accountId", accountId)
                                .queryParam("profileImage", profileImage)
                                .build()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(void.class);
    }
}
