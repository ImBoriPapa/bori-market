package com.bmarket.securityservice.domain.profile.entity;

import com.bmarket.securityservice.domain.address.Address;
import com.bmarket.securityservice.domain.address.AddressRange;
import lombok.*;

import javax.persistence.*;

import static com.bmarket.securityservice.domain.address.AddressRange.ONLY;

/**
 * 프로필 엔티티
 * 계정과 일대다 단방향 관계
 * Embedded 타입으로 주소(address)를 가지고 있음
 */
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
    @Column(name = "PROFILE_iMAGE_ID")
    private String imageId;
    @Column(name = "PROFILE_IMAGE")
    private String profileImage;
    @Embedded
    @Column(name = "ADDRESS")
    private Address address;
    @Enumerated(EnumType.STRING)
    @Column(name = "ADDRESS_RANGE")
    AddressRange addressRange;

    @Builder(builderMethodName = "createProfile")
    public Profile(String nickname, String imageId, String profileImage, Address address) {
        this.nickname = nickname;
        this.imageId = imageId;
        this.profileImage = profileImage;
        this.address = address;
        this.addressRange = ONLY;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImage(String imageId,String storedImageName) {
        this.imageId = imageId;
        this.profileImage = storedImageName;
    }

    public void updateAddress(Address address) {
        this.address = address;
    }

    public void updateAddressRange(AddressRange range) {
        this.addressRange = range;
    }

    public String getFullAddress() {
        return this.address.getCity() + "-" + this.address.getDistrict() + "-" + this.address.getTown();

    }
}
