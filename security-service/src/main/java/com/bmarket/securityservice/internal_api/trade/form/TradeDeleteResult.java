package com.bmarket.securityservice.internal_api.trade.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TradeDeleteResult {

    private Boolean isDelete;
    private LocalDateTime deletedAt;

}
