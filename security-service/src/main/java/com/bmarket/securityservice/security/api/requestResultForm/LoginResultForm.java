package com.bmarket.securityservice.security.api.requestResultForm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LoginResultForm {
    private Long accountId;
    private LocalDateTime loginAt;
}
