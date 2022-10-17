package com.bmarket.securityservice.domain.profile.entity;

import com.bmarket.securityservice.domain.address.Address;
import lombok.*;

import javax.persistence.*;

import java.util.HashMap;

import java.util.Map;


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
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ADDRESS_ID")
    private Map<String, Address> addressMap = new HashMap<>();
    private String uploadImageName;
    private String storedImageName;

    @Builder(builderMethodName = "createProfile")
    public Profile(String nickname, String email, String contact, Address address) {
        this.nickname = nickname;
        this.email = email;
        this.contact = contact;
        this.addressMap.put("first", address);
        this.storedImageName = "기본프로필 이미지";
    }

    public void upLoadingImage(String uploadImageName,String storedImageName ){
        this.uploadImageName = uploadImageName;
        this.storedImageName = storedImageName;
    }

    public void initClientId(String clientId) {
        this.clientId = clientId;
    }
}
