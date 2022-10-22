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
    private String clientId;
    private String nickname;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String contact;
    private String profileImage;

    @Builder(builderMethodName = "createProfile")
    public Profile(String nickname, String email, String contact ,String profileImage) {
        this.nickname = nickname;
        this.email = email;
        this.contact = contact;
        this.profileImage = profileImage;
    }

    public void upLoadingImage(String storedImageName ){
        this.profileImage = storedImageName;
    }
    public void initClientId(String clientId) {
        this.clientId = clientId;
    }

}
