package com.bmarket.addressservice.domain.service;

import com.bmarket.addressservice.domain.entity.Address;
import com.bmarket.addressservice.domain.repository.AddressRepository;
import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;


    public Flux<Address> findByTown(String town){

        Flux<Address> byTown = addressRepository.findByTown(town);

        return byTown;
    }
}
