package com.bmarket.securityservice.internal_api.trade;

import com.bmarket.securityservice.domain.trade.controller.RequestForm.RequestTradeForm;
import com.bmarket.securityservice.domain.trade.controller.resultForm.CreateTradeResult;
import com.bmarket.securityservice.domain.trade.controller.resultForm.TradeModifyResult;
import com.bmarket.securityservice.internal_api.trade.form.*;
import com.bmarket.securityservice.exception.custom_exception.internal_api_ex.InternalRequestFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
    public CreateTradeResult requestCreateTrade(RequestTradeServiceForm form, List<MultipartFile> images) {

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
                .bodyToMono(CreateTradeResult.class)
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
     * TradeContentsResult
     * private Long tradeId;
     * private String title;
     * private String context;
     * private String category;
     * private List<String> imagePath;
     */
    public TradeContentsResult requestGetTrade(Long tradeId) {
        return baseUrl()
                .get()
                .uri("/internal/trade/{tradeId}", tradeId)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, r -> Mono.just(new InternalRequestFailException(TRADE_WRONG_REQUEST)))
                .onStatus(HttpStatus::is5xxServerError, r -> Mono.just(new InternalRequestFailException(TRADE_SERVER_PROBLEM)))
                .bodyToMono(TradeContentsResult.class)
                .block();
    }

    /**
     * 판매 글 수정 요청 API
     */
    public TradeModifyResult requestPatchTrade(Long tradeId, RequestTradeForm.ModifyTradeForm form, List<MultipartFile> images) {

        MultiValueMap<String, HttpEntity<?>> map = getMultiValueMap(form, images);

        return baseUrl()
                .patch()
                .uri("/internal/trade/{tradeId}", tradeId)
                .body(BodyInserters.fromMultipartData(map))
                .retrieve()
                .bodyToMono(TradeModifyResult.class)
                .block();
    }

    public TradeDeleteResult requestDeleteTrade(Long tradeId) {
        return baseUrl()
                .delete()
                .uri("/internal/trade/{tradeId}", tradeId)
                .retrieve()
                .bodyToMono(TradeDeleteResult.class)
                .block();
    }
    // TODO: 2022/11/25 리팩토링하자

    /**
     * RequestTradeServiceForm
     * 멀티파트 리스트 -> MultiValueMap
     */
    private MultiValueMap<String, HttpEntity<?>> getMultiValueMap(RequestTradeServiceForm form, List<MultipartFile> images) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part("form", form);

        for (MultipartFile image : images) {
            builder.part("images", image.getResource());
        }
        return builder.build();
    }

    /**
     * CreateTradeForm
     * 멀티파트 리스트 -> MultiValueMap
     */
    private MultiValueMap<String, HttpEntity<?>> getMultiValueMap(RequestTradeForm.ModifyTradeForm form, List<MultipartFile> images) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part("form", form);

        for (MultipartFile image : images) {
            builder.part("images", image.getResource());
        }
        return builder.build();
    }
}
