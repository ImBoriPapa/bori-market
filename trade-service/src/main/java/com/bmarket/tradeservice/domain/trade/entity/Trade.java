package com.bmarket.tradeservice.domain.trade.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Trade {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRADE_ID")
    private Long id;
    private Long accountId;
    private String title;
    private String context;
    private Integer price;
    @Embedded
    private Address address;
    @Enumerated(EnumType.STRING)
    private Category category;
    private Boolean isShare;
    private Boolean isOffer;
    private String representativeImage;
    @Enumerated(EnumType.STRING)
    private TradeStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder(builderMethodName = "createTrade")
    public Trade(Long accountId, String title, String context, Integer price, Address address, Category category, Boolean isShare, Boolean isOffer, String representativeImage, TradeStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.accountId = accountId;
        this.title = title;
        this.context = context;
        this.price = price;
        this.address = address;
        this.category = category;
        this.isShare = isShare;
        this.isOffer = isOffer;
        this.representativeImage = representativeImage;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }




    public void changeStatus(TradeStatus status){
        this.status = status;
    }

    public void updateRepresentativeImage(String representativeImage){
        this.representativeImage = representativeImage;
    }

}
