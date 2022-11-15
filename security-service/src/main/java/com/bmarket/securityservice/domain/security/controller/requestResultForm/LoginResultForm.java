package com.bmarket.securityservice.domain.security.controller.requestResultForm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LoginResultForm {
    private LocalDateTime loginTime;
}
