package com.bmarket.tradeservice.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRADE_ID")
    private Long id;
    @Column(name = "ACCOUNT_ID")
    private Long accountId;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "CONTEXT")
    private String context;
    @Column(name = "PRICE")
    private Integer price;
    @Embedded
    private Address address;
    @Enumerated(EnumType.STRING)
    @Column(name = "CATEGORY")
    private Category category;
    @Column(name = "IS_SHARE")
    private Boolean isShare;
    @Column(name = "IS_OFFER")
    private Boolean isOffer;
    @Column(name = "REPRESENTATIVE_IMAGE")
    private String representativeImage;
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private TradeStatus status;
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    /**
     * 판매 글 생성자
     */
    @Builder(builderMethodName = "createTrade")
    public Trade(Long accountId, String title, String context, Integer price, Address address, Category category, Boolean isShare, Boolean isOffer, String representativeImage) {
        this.accountId = accountId;
        this.title = title;
        this.context = context;
        this.price = price;
        this.address = address;
        this.category = category;
        this.isShare = isShare;
        this.isOffer = isOffer;
        this.representativeImage = representativeImage;
        this.status = TradeStatus.SALE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateContext(String context) {
        this.context = context;
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePrice(int price) {
        this.price = price;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateAddress(Address address) {
        this.address = address;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateCategory(Category category){
        this.category = category;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateShare(boolean isShare) {
        this.isShare = isShare;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateOffer(boolean isOffer) {
        this.isOffer = isOffer;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateRepresentativeImage(String representativeImage) {
        this.representativeImage = representativeImage;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateStatus(TradeStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }


}
