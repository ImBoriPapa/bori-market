package com.bmarket.securityservice.domain.profile.entity;

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
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String contact;
    private String profileImage;
    @Embedded
    private Address address;
    @Enumerated(EnumType.STRING)
    AddressSearchRange addressRange;

    @Builder(builderMethodName = "createProfile")
    public Profile(String nickname, String email, String contact, String profileImage, Address address) {
        this.nickname = nickname;
        this.email = email;
        this.contact = contact;
        this.profileImage = profileImage;
        this.address = address;
        this.addressRange = AddressSearchRange.JUST;
    }

    public void updateProfile(Profile profile) {
        this.nickname = profile.getNickname();
        this.email = profile.getEmail();
        this.contact = profile.contact;
        this.profileImage = profile.getProfileImage();
        this.address = profile.getAddress();
        this.addressRange = profile.getAddressRange();
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateEmail(String email) {
        this.email = email;
    }
    public void updateContact(String contact) {
        this.contact = contact;
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
