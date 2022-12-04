package com.bmarket.securityservice.internal_api.trade.form;

import com.bmarket.securityservice.domain.address.AddressRange;
import com.bmarket.securityservice.domain.trade.entity.Category;
import com.bmarket.securityservice.domain.trade.entity.TradeStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchCondition {

    private Integer size;
    private Long index;
    private Category category;
    private Boolean isShare;
    private Boolean isOffer;
    private TradeStatus status;
    private Integer addressCode;
    private AddressRange range;
}
