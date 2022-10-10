package com.bmarket.securityservice.controller.external_spec.requestForm;

import lombok.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestSignUpForm {

    private String loginId;
    private String name;
    private String nickname;
    private String password;
    private String email;
    private String contact;
}
