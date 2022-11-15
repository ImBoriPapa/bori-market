package com.bmarket.securityservice.domain.security.controller.requestForm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;




@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestLoginForm {
    String loginId;
    String password;
}
