package com.bmarket.tradeservice.domain.trade.repository.query;

import com.bmarket.tradeservice.domain.trade.entity.Category;
import com.bmarket.tradeservice.domain.trade.entity.TradeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchCondition {
    private Category category;
    private Boolean isShare;
    private Boolean isOffer;
    private TradeStatus status;
    private Integer addressCode;
    private AddressSearchCondition addressSearchCondition;

}

