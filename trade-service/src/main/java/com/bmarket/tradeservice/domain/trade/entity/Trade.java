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
    private String nickname;
    private String profileImage;
    private String title;
    private String context;
    private Integer price;
    private Integer addressCode;
    private String townName;
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
    public Trade(Long accountId, String nickname, String profileImage, String title, String context, Integer price, Integer addressCode, String townName, Category category, Boolean isShare, Boolean isOffer, String representativeImage) {
        this.accountId = accountId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.title = title;
        this.context = context;
        this.price = price;
        this.addressCode = addressCode;
        this.townName = townName;
        this.category = category;
        this.isShare = isShare;
        this.isOffer = isOffer;
        this.representativeImage = representativeImage;
        this.status = TradeStatus.SALE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void changeStatus(TradeStatus status){
        this.status = status;
    }

    public void updateRepresentativeImage(String representativeImage){
        this.representativeImage = representativeImage;
    }

}
