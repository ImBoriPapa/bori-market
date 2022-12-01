package com.bmarket.addressservice.repository;

import com.bmarket.addressservice.entity.Address;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;


public interface AddressRepository extends ReactiveMongoRepository<Address,String> {
    Flux<Address> findAddressByTownContains(String town);

    Flux<Address> findAddressByAddressCode(Integer addressCode);

}
