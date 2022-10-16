package com.bmarket.addressservice.controller;

import com.bmarket.addressservice.domain.entity.Address;
import com.bmarket.addressservice.domain.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AddressController {

    private final AddressRepository addressRepository;

    @GetMapping("/address")
    public Flux getAddress() {
        Flux<Address> all = addressRepository.findAll();
        return all;
    }


}
