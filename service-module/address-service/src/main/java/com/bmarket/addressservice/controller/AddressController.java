package com.bmarket.addressservice.controller;

import com.bmarket.addressservice.domain.entity.Address;
import com.bmarket.addressservice.domain.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AddressController {

    private final AddressRepository addressRepository;
    private final ReactiveMongoTemplate template;

    /**
     * 주소 전체 검색
     * @return
     */
    @GetMapping("/address")
    public Mono<List<Address>> getAddress() {
        Mono<List<Address>> listMono = addressRepository.findAll().collectList();
        return listMono;
    }

    @GetMapping("/test")
    public Flux<Address> findOne(@RequestParam String town){

        Flux<Address> byTown = addressRepository.findByTown(town);

        return byTown;

    }



}
