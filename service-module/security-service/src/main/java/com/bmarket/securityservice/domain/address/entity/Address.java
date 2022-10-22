package com.bmarket.securityservice.domain.address.entity;

import com.bmarket.securityservice.domain.profile.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADDRESS_ID")
    private Long id;
    private Integer addressCode;
    @Enumerated(EnumType.STRING)
    private AddressStatus addressStatus;
    private String city;
    private String district;
    private String town;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROFILE_ID")
    private Profile profile;

    @Builder(builderMethodName = "createAddress")
    public Address(Integer addressCode, AddressStatus addressStatus, String city, String district, String town, Profile profile) {
        this.addressCode = addressCode;
        this.addressStatus = addressStatus;
        this.city = city;
        this.district = district;
        this.town = town;
        this.profile = profile;
    }


}
