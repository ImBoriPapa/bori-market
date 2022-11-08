package com.bmarket.securityservice.api.trade.controller.resultForm;

import com.bmarket.securityservice.api.common.ResultForm;
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
