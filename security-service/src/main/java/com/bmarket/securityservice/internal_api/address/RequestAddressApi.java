package com.bmarket.securityservice.internal_api.address;

import com.bmarket.securityservice.domain.address.AddressResult;
import com.bmarket.securityservice.exception.custom_exception.internal_api_ex.InternalRequestFailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.bmarket.securityservice.utils.status.ResponseStatus.TRADE_SERVER_PROBLEM;
import static com.bmarket.securityservice.utils.status.ResponseStatus.TRADE_WRONG_REQUEST;

@Component
@Slf4j
public class RequestAddressApi {

    @Value("${internal.address}")
    public String addressServiceUrl;

    /**
     * 주소 검색 요청 API
     */
    public List<AddressResult> searchByTown(String town){
        return WebClient.create(addressServiceUrl)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("town", town)
                        .build()
                )
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, r -> Mono.just(new InternalRequestFailException(TRADE_WRONG_REQUEST)))
                .onStatus(HttpStatus::is5xxServerError, r -> Mono.just(new InternalRequestFailException(TRADE_SERVER_PROBLEM)))
                .bodyToFlux(AddressResult.class).collectList()
                .block();

    }

}
