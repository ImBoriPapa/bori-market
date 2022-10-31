package com.bmarket.securityservice.api.profile.entity;

import com.bmarket.securityservice.api.address.Address;
import com.bmarket.securityservice.api.address.AddressSearchRange;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROFILE_ID")
    private Long id;
    private String nickname;
    private String profileImage;
    @Embedded
    private Address address;
    @Enumerated(EnumType.STRING)
    AddressSearchRange addressRange;

    @Builder(builderMethodName = "createProfile")
    public Profile(String nickname , String profileImage, Address address) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.address = address;
        this.addressRange = AddressSearchRange.JUST;
    }



    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImage(String storedImageName) {
        this.profileImage = storedImageName;
    }

    public void updateAddress(Address address) {
        this.address = address;
    }
    public void settingAddressSearchRange(AddressSearchRange range) {
        this.addressRange = range;
    }

}
