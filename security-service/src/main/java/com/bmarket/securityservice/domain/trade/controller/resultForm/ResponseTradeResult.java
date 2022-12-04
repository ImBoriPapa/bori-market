package com.bmarket.securityservice.domain.trade.controller.resultForm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ResponseTradeResult {
    private Boolean success;
    private Long tradeId;
    private LocalDateTime createdAt;
}
