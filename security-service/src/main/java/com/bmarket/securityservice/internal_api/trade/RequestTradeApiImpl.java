package com.bmarket.securityservice.internal_api.trade;

import com.bmarket.securityservice.domain.trade.controller.resultForm.RequestGetTradeListResult;
import com.bmarket.securityservice.domain.trade.controller.resultForm.ResponseCreateTradeResult;
import com.bmarket.securityservice.internal_api.trade.form.RequestTradeForm;
import com.bmarket.securityservice.internal_api.trade.form.SearchCondition;
import com.bmarket.securityservice.internal_api.trade.form.TradeDetailResult;
import com.bmarket.securityservice.internal_api.trade.form.TradeListResult;
import com.bmarket.securityservice.exception.custom_exception.internal_api_ex.InternalRequestFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
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
public class RequestTradeApiImpl implements RequestTradeApi{

    public final String TRADE_SERVICE_URL = "http://localhost:8081/internal/trade";

    // TODO: 2022/10/31 onStatus 에러 처리
    // TODO: 2022/11/17 거래 상태 변경 로직 추가
    public WebClient tradeClient() {
        return WebClient.builder()
                .baseUrl(TRADE_SERVICE_URL)
                .build();
    }

    /**
     * 거래 생성 요청 api
     */
    @Override
    public ResponseCreateTradeResult requestCreateTrade(Long accountId, RequestTradeForm form, List<MultipartFile> images) {

        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part("form", form);
        for (MultipartFile image : images) {
            builder.part("images", image.getResource());
        }
        return tradeClient()
                .post().uri("/{accountId}", accountId)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        response -> Mono.error(() -> new InternalRequestFailException(TRADE_WRONG_REQUEST)))
                .onStatus(HttpStatus::is5xxServerError,
                        response -> Mono.error(() -> new InternalRequestFailException(TRADE_SERVER_PROBLEM)))
                .bodyToMono(ResponseCreateTradeResult.class)
                .block();
    }

    /**
     * 거래 내역 요청 api
     */
    @Override
    public List<RequestGetTradeListResult> requestGetSaleHistory(Long accountId) {
        return tradeClient()
                .get()
                .uri("/{accountId}/sale-history", accountId)
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
    @Override
    public TradeListResult requestGetTradeList(SearchCondition condition) {
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
                .bodyToMono(TradeListResult.class)
                .block();
    }

    /**
     * 거래 상세 내역 조회 요청 api
     */
    @Override
    public TradeDetailResult requestGetTrade(Long tradeId) {
        return WebClient.create("http://localhost:8081/internal/trade")
                .get()
                .uri("/{tradeId}", tradeId)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, r -> Mono.just(new InternalRequestFailException(TRADE_WRONG_REQUEST)))
                .onStatus(HttpStatus::is5xxServerError, r -> Mono.just(new InternalRequestFailException(TRADE_SERVER_PROBLEM)))
                .bodyToMono(TradeDetailResult.class)
                .block();
    }
}
