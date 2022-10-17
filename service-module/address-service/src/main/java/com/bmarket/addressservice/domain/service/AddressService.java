package com.bmarket.addressservice.domain.service;


import com.bmarket.addressservice.domain.repository.AddressRepository;

import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;


@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public Flux<AddressResult> findByTown(String town) {

        return addressRepository.findAddressByTownLike(town)
                .flatMap(m -> {
                            AddressResult result = AddressResult.builder()
                                    .addressCode(m.getAddressCode())
                                    .city(m.getCity())
                                    .district(m.getDistrict())
                                    .town(m.getTown()).build();
                            return Flux.just(result);
                        }
                );

    }

    public Flux<AddressResult> findByCode(Integer code) {
        return addressRepository.findByAddressCode(code)
                .flatMap(m -> {
                            AddressResult result = AddressResult.builder()
                                    .addressCode(m.getAddressCode())
                                    .city(m.getCity())
                                    .district(m.getDistrict())
                                    .town(m.getTown()).build();
                            return Flux.just(result);
                        }
                );
    }


}
