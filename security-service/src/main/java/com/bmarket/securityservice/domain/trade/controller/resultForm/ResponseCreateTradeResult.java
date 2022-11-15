package com.bmarket.securityservice.domain.trade.controller.resultForm;

import com.bmarket.securityservice.domain.common.ResultForm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ResponseCreateTradeResult extends ResultForm {
    private Long tradeId;
    private LocalDateTime createdAt;
}
