package com.bmarket.addressservice.domain.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AddressResult {
    private String city;
    private String district;
    private String town;
    private Integer addressCode;
}
