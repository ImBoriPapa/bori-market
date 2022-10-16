package com.bmarket.securityservice.domain.profile.entity;

import lombok.*;

import javax.persistence.*;



@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Profile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROFILE_ID")
    private Long id;
    private String clientId;
    private String nickname;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String contact;
    private Integer firstAddressCode;
    private Integer secondAddressCode;

    private String updatedProfileImage;
    private String storedProfileImage;

    @Builder(builderMethodName = "createProfile")
    public Profile(String nickname, String email, String contact,int firstAddressCode) {
        this.nickname = nickname;
        this.email = email;
        this.contact = contact;
        this.firstAddressCode = firstAddressCode;
        this.secondAddressCode = 0;
        this.storedProfileImage = "기본프로필 이미지";
    }

    public void initClientId(String clientId){
        this.clientId = clientId;
    }
}
