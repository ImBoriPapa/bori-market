package com.bmarket.tradeservice.domain.entity;



import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TradeImage {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String imageId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRADE_ID")
    private Trade trade;
    private String imagePath;

    /**
     * imageId : frm service 에 저장된 이미지 아이디
     */
    @Builder(builderMethodName = "createImage")
    public TradeImage(String imageId,Trade trade,String imagePath) {
        this.imageId = imageId;
        this.trade = trade;
        this.imagePath = imagePath;
    }
}
