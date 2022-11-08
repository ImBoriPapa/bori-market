package com.bmarket.addressservice.domain.service;

import com.bmarket.addressservice.domain.entity.Address;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

@SpringBootTest
class AddressServiceTest {

    @Autowired
    AddressService addressService;

    @Test
    @DisplayName("검색 테스트")
    void findByTown() throws Exception{
        //given
        String town = "우장산동";

        //when


        //then


    }

}