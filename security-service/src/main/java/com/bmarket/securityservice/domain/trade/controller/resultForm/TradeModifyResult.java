package com.bmarket.securityservice.domain.trade.controller.resultForm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TradeModifyResult {

    private Long tradeId;
    private LocalDateTime updatedAt;

}
