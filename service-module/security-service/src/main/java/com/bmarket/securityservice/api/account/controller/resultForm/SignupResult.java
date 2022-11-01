package com.bmarket.securityservice.api.account.controller.resultForm;

import com.bmarket.securityservice.api.common.ResultForm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupResult  extends ResultForm {
    private Long accountId;
    private LocalDateTime createdAt;
}
