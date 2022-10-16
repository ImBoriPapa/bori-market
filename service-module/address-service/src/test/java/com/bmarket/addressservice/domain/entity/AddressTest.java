package com.bmarket.addressservice.domain.entity;

import com.bmarket.addressservice.domain.repository.AddressRepository;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@SpringBootTest

class AddressTest {

    @Autowired
    AddressRepository addressRepository;
    @Autowired
    ReactiveMongoTemplate template;

    @AfterEach
    void afterEach(){
        addressRepository.deleteAll();
    }

    @Test
    @DisplayName("검색")
    void search() throws Exception{
        //given

        String town = "우장산";
        //when
        Flux<Address> flux = template.find(query(where("town").is(town)), Address.class);



        //then

    }


}