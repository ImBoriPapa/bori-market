package com.bmarket.addressservice.domain.service;

import com.bmarket.addressservice.domain.entity.Address;
import com.bmarket.addressservice.domain.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public void save(){
        Address address = new Address();

    }
}
