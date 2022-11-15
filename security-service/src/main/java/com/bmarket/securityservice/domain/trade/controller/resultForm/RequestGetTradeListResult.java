package com.bmarket.securityservice.domain.trade.controller.resultForm;

import com.bmarket.securityservice.domain.common.ResultForm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RequestGetTradeListResult extends ResultForm {

    private Long tradeId;
    private String title;
    private String townName;
    private Integer price;
    private String representativeImage;
    private LocalDateTime createdAt;
}
