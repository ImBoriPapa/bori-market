package com.bmarket.securityservice.account.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
public class ResponseAccountForm {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class ResponseSignupForm{
        private Long accountId;
        private LocalDateTime createdAt;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class ResponseResultForm {
        private Long accountId;
    }




}
