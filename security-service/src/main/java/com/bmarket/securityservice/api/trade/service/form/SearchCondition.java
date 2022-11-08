package com.bmarket.securityservice.api.trade.service.form;

import com.bmarket.securityservice.api.address.AddressRange;
import com.bmarket.securityservice.api.trade.entity.Category;
import com.bmarket.securityservice.api.trade.entity.TradeStatus;
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
    private Long lastIndex;
    private Category category;
    private Boolean isShare;
    private Boolean isOffer;
    private TradeStatus status;
    private Integer addressCode;
    private AddressRange range;
}
