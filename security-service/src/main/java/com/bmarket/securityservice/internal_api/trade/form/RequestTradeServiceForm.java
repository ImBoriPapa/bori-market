package com.bmarket.securityservice.internal_api.trade.form;

import com.bmarket.securityservice.domain.address.Address;
import com.bmarket.securityservice.domain.trade.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class RequestTradeServiceForm {

    private Long accountId;
    private Integer addressCode;
    private Address address;
    private String title;
    private String context;
    private Integer price;
    private Category category;
    private Boolean isShare;
    private Boolean isOffer;
}
