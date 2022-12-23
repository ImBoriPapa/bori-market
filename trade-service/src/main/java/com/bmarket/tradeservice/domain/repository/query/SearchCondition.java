package com.bmarket.tradeservice.domain.repository.query;

import com.bmarket.tradeservice.domain.entity.Category;
import com.bmarket.tradeservice.domain.entity.TradeStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchCondition {
    private Category category;
    private Boolean isOffer;
    private TradeStatus status;
    private Integer addressCode;
    private AddressRange range;


}

