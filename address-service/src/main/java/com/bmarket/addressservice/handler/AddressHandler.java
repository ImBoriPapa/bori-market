package com.bmarket.addressservice.handler;

import com.bmarket.addressservice.dto.AddressResult;
import com.bmarket.addressservice.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.List;
import java.util.Optional;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
@Slf4j
public class AddressHandler {
    private final AddressRepository repository;
    @Value("classpath:/form/searchForm.html")
    public Resource searchForm;

    /**
     * 동네로 검색한 주소 결과 반환
     */
    public Mono<ServerResponse> searchByTown(ServerRequest request) {

        String town = request.queryParam("town").orElse("");

        return getServerResponseMono(town);
    }

    private Mono<ServerResponse> getServerResponseMono(String town) {
        Flux<AddressResult> map = repository.findAddressByTownContains(town)
                .map(address -> AddressResult.builder()
                        .addressCode(address.getAddressCode())
                        .city(address.getCity())
                        .district(address.getDistrict())
                        .town(address.getTown())
                        .build());
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(map, AddressResult.class);
    }

    /**
     * searchForm.html 반환
     */
    public Mono<ServerResponse> searchForm(ServerRequest request) {
        return ok().contentType(MediaType.TEXT_HTML).bodyValue(searchForm);
    }


}
