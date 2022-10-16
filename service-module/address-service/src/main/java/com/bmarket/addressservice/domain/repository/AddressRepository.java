package com.bmarket.addressservice.domain.repository;

import com.bmarket.addressservice.domain.entity.Address;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface AddressRepository extends ReactiveMongoRepository<Address,String> {

    Flux<Address> findByTown(String town);
}
