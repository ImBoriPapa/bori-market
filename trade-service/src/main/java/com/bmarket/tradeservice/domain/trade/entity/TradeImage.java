package com.bmarket.tradeservice.domain.trade.entity;



import com.bmarket.tradeservice.domain.trade.entity.Trade;
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
    private String imageName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRADE_ID")
    private Trade trade;

    @Builder(builderMethodName = "createImage")
    public TradeImage(String imageName, Trade trade) {
        this.imageName = imageName;
        this.trade = trade;
    }
}
