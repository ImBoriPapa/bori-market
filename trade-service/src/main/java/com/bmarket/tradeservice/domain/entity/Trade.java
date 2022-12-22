package com.bmarket.tradeservice.domain.entity;

import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @NotNull
    @Column(name = "MEMBER_ID")
    private String memberId;
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
    @Enumerated(EnumType.STRING)
    @Column(name = "TRADE_TYPE")
    private TradeType tradeType;
    @Column(name = "IS_OFFER")
    private Boolean isOffer;
    @Enumerated(EnumType.STRING)
    @Column(name = "TRADE_STATUS")
    private TradeStatus tradeStatus;
    @Column(name = "REPRESENTATIVE_IMAGE")
    private String representativeImage;
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    /**
     * 판매글 생성 메서드
     */
    @Builder(builderMethodName = "createTrade")
    public Trade(String memberId, String title, String context, Integer price, Address address, Category category, TradeType tradeType, Boolean isOffer, String representativeImage) {
        this.memberId = memberId;
        this.title = title;
        this.context = context;
        this.price = price;
        this.address = address;
        this.category = category;
        this.tradeType = tradeType;
        this.isOffer = isOffer;
        this.tradeStatus = TradeStatus.SALE;
        this.representativeImage = representativeImage;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Trade 수정
     */
    public void updateTrade(UpdateForm builder) {
        updateTitle(builder.getTitle());
        updateContext(builder.getContext());
        updatePrice(builder.getPrice());
        updateCategory(builder.getCategory());
        updateIsOffer(builder.getIsOffer());
        updateAddress(builder.getAddress());
        updateRepresentativeImage(builder.getRepresentativeImage());
        updateStatus(builder.getTradeStatus());
        updateType(builder.getTradeType());
        this.updatedAt = LocalDateTime.now();
    }

    private void updateTitle(String title) {
        if (StringUtils.hasLength(title)) {
            this.title = title;
        }
    }

    private void updateContext(String context) {
        if (StringUtils.hasLength(context)) {
            this.context = context;
        }
    }

    private void updatePrice(Integer price) {
        if (price != null) {
            this.price = price;
        }
    }

    private void updateAddress(Address address) {
        if (address != null) {
            this.address = address;
        }
    }

    private void updateCategory(Category category) {
        if (category != null) {
            this.category = category;
        }
    }

    private void updateIsOffer(Boolean isOffer) {
        if (isOffer != null) {
            this.isOffer = isOffer;
        }
    }

    private void updateType(TradeType tradeType) {
        if (tradeType != null) {
            this.tradeType = tradeType;
        }
    }

    public void updateRepresentativeImage(String representativeImage) {
        if (StringUtils.hasText(representativeImage)) {
            this.representativeImage = representativeImage;
        }
    }

    public void updateStatus(TradeStatus status) {
        if (status != null) {
            this.tradeStatus = status;
        }
    }

    /**
     * Trade 수정을 위한 빌더 클래스
     */
    @Getter
    public static class UpdateForm {
        private final String title;
        private final String context;
        private final Integer price;
        private final Category category;
        private final Address address;
        private final String representativeImage;
        private final TradeType tradeType;
        private final TradeStatus tradeStatus;
        private final Boolean isOffer;

        @Builder
        public UpdateForm(String title, String context, Integer price, Category category, Address address, String representativeImage, TradeType tradeType, TradeStatus tradeStatus, Boolean isOffer) {
            this.title = title;
            this.context = context;
            this.price = price;
            this.category = category;
            this.address = address;
            this.representativeImage = representativeImage;
            this.tradeType = tradeType;
            this.tradeStatus = tradeStatus;
            this.isOffer = isOffer;
        }
    }

}
