package com.bmarket.securityservice.domain.address;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@Getter
public class Address {

    private Integer addressCode;
    private String city;
    private String district;
    private String town;

    @Builder(builderMethodName = "createAddress")
    public Address(Integer addressCode, String city, String district, String town) {
        this.addressCode = addressCode;
        this.city = city;
        this.district = district;
        this.town = town;
    }
}
