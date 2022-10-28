package com.bmarket.tradeservice.domain.trade.repository.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Builder
@Getter
public class TradeListDto {
    private Long id;
    private String title;
    private String townName;
    private int price;
    private String representativeImage;
    private LocalDateTime createdAt;

    @QueryProjection
    public TradeListDto(Long id, String title, String townName, int price, String representativeImage, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.townName = townName;
        this.price = price;
        this.representativeImage = representativeImage;
        this.createdAt = createdAt;
    }
}
