package com.bmarket.addressservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResult {
    private String city;
    private String district;
    private String town;
    private Integer addressCode;

}
