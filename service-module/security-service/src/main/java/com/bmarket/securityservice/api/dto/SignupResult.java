package com.bmarket.securityservice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupResult extends ResultForm {
    private Long accountId;
    private String clientId;
    private LocalDateTime createdAt;
}
