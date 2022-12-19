package com.bmarket.securityservice.security.api.reponseForm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LoginResultForm {
    private String memberId;
    private LocalDateTime loginAt;
}
