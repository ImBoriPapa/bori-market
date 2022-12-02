package com.bmarket.tradeservice.domain.entity;

import lombok.*;
import org.springframework.util.StringUtils;

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


    /**
     * Trade 수정
     */
    public void updateTrade(UpdateBuilder builder) {
        updateTitle(builder.getTitle());
        updateContext(builder.getContext());
        updatePrice(builder.getPrice());
        updateAddress(builder.getAddress());
        updateCategory(builder.getCategory());
        updateIsShare(builder.getIsShare());
        updateIsOffer(builder.getIsOffer());
        this.updatedAt = LocalDateTime.now();
    }

    private void updateIsShare(Boolean isShare) {
        if (isShare != null) {
            this.isShare = isShare;
        }
    }

    private void updateIsOffer(Boolean isOffer) {
        if (isOffer != null) {
            this.isOffer = isOffer;
        }
    }

    private void updateCategory(Category category) {
        if (category != null) {
            this.category = category;
        }
    }

    private void updateAddress(Address address) {
        if (address != null) {
            this.address = address;
        }
    }

    private void updatePrice(Integer price) {
        if (price != null) {
            this.price = price;
        }
    }

    private void updateContext(String context) {
        if (StringUtils.hasLength(context)) {
            this.context = context;
        }
    }

    private void updateTitle(String title) {
        if (StringUtils.hasLength(title)) {
            this.title = title;
        }
    }

    public void updateRepresentativeImage(String representativeImage) {
        this.representativeImage = representativeImage;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateStatus(TradeStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Trade 수정을 위한 빑더 클래스
     */
    @Getter
    public static class UpdateBuilder {
        private String title;
        private String context;
        private Integer price;
        private Category category;
        private Address address;
        private Boolean isShare;
        private Boolean isOffer;

        @Builder(builderClassName = "updateBuilder")
        public UpdateBuilder(String title, String context, Integer price, Category category, Address address, Boolean isShare, Boolean isOffer) {
            this.title = title;
            this.context = context;
            this.price = price;
            this.category = category;
            this.address = address;
            this.isShare = isShare;
            this.isOffer = isOffer;
        }
    }

}
