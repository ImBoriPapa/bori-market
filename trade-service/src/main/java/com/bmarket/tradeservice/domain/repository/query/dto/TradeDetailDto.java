package com.bmarket.tradeservice.domain.repository.query.dto;

import com.bmarket.tradeservice.domain.entity.Address;
import com.bmarket.tradeservice.domain.entity.Category;
import com.bmarket.tradeservice.domain.entity.TradeStatus;
import com.bmarket.tradeservice.domain.entity.TradeType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
public class TradeDetailDto {
    private Long tradeId;
    private String memberId;
    private String title;
    private String context;
    private Integer price;
    private Category category;
    private TradeStatus tradeStatus;
    private TradeType tradeType;
    private Address address;
    private Boolean isOffer;
    private LocalDateTime createdAt;
    private List<TradeImageDto> imagePath;

    @QueryProjection
    public TradeDetailDto(Long tradeId, String memberId, String title, String context, Integer price, Category category, TradeStatus tradeStatus, TradeType tradeType, Address address, Boolean isOffer, LocalDateTime createdAt) {
        this.tradeId = tradeId;
        this.memberId = memberId;
        this.title = title;
        this.context = context;
        this.price = price;
        this.category = category;
        this.tradeStatus = tradeStatus;
        this.tradeType = tradeType;
        this.address = address;
        this.isOffer = isOffer;
        this.createdAt = createdAt;
    }


    public void addImagePath(List<TradeImageDto> imagePath) {
        this.imagePath = imagePath;
    }
}
