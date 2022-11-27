package com.bmarket.addressservice.domain.repository;

import com.bmarket.addressservice.domain.entity.Address;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


public interface AddressRepository extends ReactiveMongoRepository<Address,String> {

    Flux<Address> findAddressByTownLike(String town);

    Flux<Address> findByAddressCode(Integer code);
}
