package com.bmarket.tradeservice.api.requestForm;

import com.bmarket.tradeservice.domain.entity.Address;
import com.bmarket.tradeservice.domain.entity.Category;
import com.bmarket.tradeservice.domain.entity.TradeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class RequestForm {
    @NotBlank
    private String memberId;
    @NotBlank
    private String title;
    @NotBlank
    private String context;
    private Integer price;
    private Address address;
    @NotBlank
    private Category category;
    private Boolean isOffer;
    private TradeType tradeType;

}
