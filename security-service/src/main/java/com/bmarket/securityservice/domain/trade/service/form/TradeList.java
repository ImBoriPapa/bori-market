package com.bmarket.securityservice.domain.trade.service.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TradeList {
    private Integer tradeId;
    private String title;
    private String townName;
    private Integer price;
    private String representativeImage;
    private LocalDateTime createdAt;
}
