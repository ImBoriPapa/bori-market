package com.bmarket.addressservice.domain.entity;

import com.bmarket.addressservice.domain.repository.AddressRepository;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest

class AddressTest {

    @Autowired
    AddressRepository addressRepository;

    @AfterEach
    void afterEach(){
        addressRepository.deleteAll();
    }



}