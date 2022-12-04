package com.bmarket.securityservice.internal_api.trade.form;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.List;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeListResult {
    private Integer size;
    private Boolean hasNext;
    private List<TradeListDto> tradeListResult;

}
