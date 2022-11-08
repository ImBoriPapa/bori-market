package com.bmarket.securityservice.api.trade.service.form;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.List;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TradeListResult {
    private Integer size;
    private Boolean hasNext;
    private List<TradeList> result;


}
