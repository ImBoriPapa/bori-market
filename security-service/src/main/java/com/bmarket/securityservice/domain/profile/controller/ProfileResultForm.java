package com.bmarket.securityservice.domain.profile.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class ProfileResultForm {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProfileResult {
        private Long accountId;
        private String nickname;
        private String profileImage;
        private String addressRange;
        private String addressRangeEx;
        private String fullAddress;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProfileUpdateResult {
        private Long accountId;
    }

}