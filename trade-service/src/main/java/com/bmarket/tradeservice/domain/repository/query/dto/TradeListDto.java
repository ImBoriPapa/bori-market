package com.bmarket.tradeservice.domain.repository.query.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Builder
@Getter
public class TradeListDto {
    private Long tradeId;
    private String title;
    private String address;
    private int price;
    private String representativeImage;
    private LocalDateTime createdAt;

    @QueryProjection
    public TradeListDto(Long tradeId, String title, String address, int price, String representativeImage, LocalDateTime createdAt) {
        this.tradeId = tradeId;
        this.title = title;
        this.address = address;
        this.price = price;
        this.representativeImage = representativeImage;
        this.createdAt = createdAt;
    }
}
