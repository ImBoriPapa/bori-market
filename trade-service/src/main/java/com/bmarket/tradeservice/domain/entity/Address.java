package com.bmarket.tradeservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    private Integer addressCode;
    private String city;
    private String district;
    private String town;
}
