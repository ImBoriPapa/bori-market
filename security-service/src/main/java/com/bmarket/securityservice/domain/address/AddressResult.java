package com.bmarket.securityservice.domain.address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddressResult {

    private Integer addressCode;
    private String city;
    private String district;
    private String town;

}
