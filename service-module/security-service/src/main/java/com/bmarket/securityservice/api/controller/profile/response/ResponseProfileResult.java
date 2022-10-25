package com.bmarket.securityservice.api.controller.profile.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ResponseProfileResult {

    private String nickname;
    private String email;
    private String contact;
    private String profileImage;
    private String fullAddress;
    private String addressRange;

    @QueryProjection
    public ResponseProfileResult(String nickname, String email, String contact, String profileImage, String fullAddress, String addressRange) {
        this.nickname = nickname;
        this.email = email;
        this.contact = contact;
        this.profileImage = profileImage;
        this.fullAddress = fullAddress;
        this.addressRange = addressRange;
    }
}
