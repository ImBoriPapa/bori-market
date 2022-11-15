package com.bmarket.securityservice.domain.account.controller;

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

        private String result;
    }




}
