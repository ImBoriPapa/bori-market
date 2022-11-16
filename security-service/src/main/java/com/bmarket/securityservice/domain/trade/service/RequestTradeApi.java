package com.bmarket.securityservice.domain.trade.service;

import com.bmarket.securityservice.domain.trade.controller.resultForm.RequestGetTradeListResult;
import com.bmarket.securityservice.domain.trade.controller.resultForm.ResponseCreateTradeResult;
import com.bmarket.securityservice.domain.trade.service.form.RequestTradeForm;
import com.bmarket.securityservice.domain.trade.service.form.SearchCondition;
import com.bmarket.securityservice.domain.trade.service.form.TradeDetailResult;
import com.bmarket.securityservice.domain.trade.service.form.TradeListResult;
import com.bmarket.securityservice.exception.custom_exception.internal_api_ex.InternalRequestFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
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

    public static final String TRADE_SERVICE_URL = "http://localhost:8081/internal/trade";
    // TODO: 2022/10/31 onStatus 에러 처리
    public WebClient tradeClient() {
        return WebClient.builder()
                .baseUrl(TRADE_SERVICE_URL)
                .build();
    }

    /**
     * 거래 생성 요청 api
     */
    public ResponseCreateTradeResult requestCreateTrade(Long accountId,RequestTradeForm form, List<MultipartFile> images) {

        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part("form", form);
        for (MultipartFile image : images) {
            builder.part("images", image.getResource());
        }
        return tradeClient()
                .post().uri("/{accountId}",accountId)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        response -> Mono.error(()-> new InternalRequestFailException(TRADE_WRONG_REQUEST)))
                .onStatus(HttpStatus::is5xxServerError,
                        response -> Mono.error(()-> new InternalRequestFailException(TRADE_SERVER_PROBLEM)))
                .bodyToMono(ResponseCreateTradeResult.class)
                .block();
    }

    /**
     * 거래 내역 요청 api
     */
    public List<RequestGetTradeListResult> requestGetSaleHistory(Long accountId) {
        return tradeClient()
                .get()
                .uri("/{accountId}/sale-history",accountId)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        response -> Mono.error(()-> new InternalRequestFailException(TRADE_WRONG_REQUEST)))
                .onStatus(HttpStatus::is5xxServerError,
                        response -> Mono.error(()-> new InternalRequestFailException(TRADE_SERVER_PROBLEM)))
                .bodyToMono(RequestGetTradeListResult[].class)
                .map(Arrays::asList)
                .block();
    }


    public ResponseEntity<TradeListResult> RequestGetTradeList(SearchCondition condition) {
        return WebClient.create("http://localhost:8081")
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
                .toEntity(TradeListResult.class)
                .block();
    }

    public ResponseEntity<TradeDetailResult> RequestGetTrade(Long tradeId) {
        return WebClient.create("http://localhost:8081/internal/trade/" + tradeId)
                .get()
                .uri("/tradeId", tradeId)
                .retrieve()
                .onStatus(m -> m.is4xxClientError(), r -> Mono.just(new IllegalArgumentException("RequestGetTrade error")))
                .toEntity(TradeDetailResult.class)
                .block();
    }
}
