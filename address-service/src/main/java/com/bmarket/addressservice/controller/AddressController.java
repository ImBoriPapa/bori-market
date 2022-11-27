package com.bmarket.addressservice.controller;


import com.bmarket.addressservice.domain.entity.Address;
import com.bmarket.addressservice.domain.repository.AddressRepository;
import com.bmarket.addressservice.domain.service.AddressResult;
import com.bmarket.addressservice.domain.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping("/internal/address")
    public Flux<AddressResult> findOne(@RequestParam String town) {
        if (town.equals("ready")) {
            return Flux.empty();
        }
        log.info("Address is call");
        return addressService.findByTown(town);
    }

    @GetMapping("/addressData/{addressCode}")
    public Flux<AddressResult> getAddressByCode(@PathVariable Integer addressCode) {
        return addressService.findByCode(addressCode);
    }




}
