package com.bmarket.addressservice.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ADDRESS")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Address {

    @Id
    private String id;
    private Integer addressCode;
    private String city;
    private String district;
    private String town;

    @Builder
    public Address(Integer addressCode, String city, String district, String town) {
        this.addressCode = addressCode;
        this.city = city;
        this.district = district;
        this.town = town;
    }
}
