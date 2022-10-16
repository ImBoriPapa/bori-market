package com.bmarket.addressservice.domain.repository;

import com.bmarket.addressservice.domain.entity.Address;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;



public interface AddressRepository extends ReactiveMongoRepository<Address,String> {
}
