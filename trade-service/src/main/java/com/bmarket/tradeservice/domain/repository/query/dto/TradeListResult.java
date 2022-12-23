package com.bmarket.tradeservice.domain.repository.query.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Builder
@Getter
public class TradeListResult {
    private Long tradeId;
    private String title;
    private String town;
    private int price;
    private String representativeImage;
    private LocalDateTime createdAt;

    @QueryProjection
    public TradeListResult(Long tradeId, String title, String town, int price, String representativeImage, LocalDateTime createdAt) {
        this.tradeId = tradeId;
        this.title = title;
        this.town = town;
        this.price = price;
        this.representativeImage = representativeImage;
        this.createdAt = createdAt;
    }
}
