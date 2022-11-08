package com.bmarket.securityservice.api.profile.entity;

import com.bmarket.securityservice.api.address.Address;
import com.bmarket.securityservice.api.address.AddressRange;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = {"id"})
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROFILE_ID")
    private Long id;
    @Column(name = "NICK_NAME")
    private String nickname;
    @Column(name = "PROFILE_IMAGE")
    private String profileImage;
    @Embedded
    @Column(name = "ADDRESS")
    private Address address;
    @Enumerated(EnumType.STRING)
    @Column(name = "ADDRESS_RANGE")
    AddressRange addressRange;

    @Builder(builderMethodName = "createProfile")
    public Profile(String nickname, String profileImage, Address address) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.address = address;
        this.addressRange = AddressRange.JUST;
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

    public void updateAddressRange(AddressRange range) {
        this.addressRange = range;
    }

}
