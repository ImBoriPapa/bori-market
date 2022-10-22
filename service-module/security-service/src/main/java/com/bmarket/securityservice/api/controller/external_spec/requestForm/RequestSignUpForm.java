package com.bmarket.securityservice.api.controller.external_spec.requestForm;

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
    private Integer addressCode;
    private String city;
    private String district;
    private String town;
}
