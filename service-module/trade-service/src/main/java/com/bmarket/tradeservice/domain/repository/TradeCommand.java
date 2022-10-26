package com.bmarket.tradeservice.domain.repository;

import com.bmarket.tradeservice.domain.entity.Category;
import com.bmarket.tradeservice.domain.entity.QTrade;
import com.bmarket.tradeservice.domain.entity.QTradeImage;
import com.bmarket.tradeservice.domain.entity.Trade;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.bmarket.tradeservice.domain.entity.QTrade.*;
import static com.bmarket.tradeservice.domain.entity.QTradeImage.*;

@RequiredArgsConstructor
public class TradeCommand {

    private final JPAQueryFactory queryFactory;

    public List<Trade> get1(Category category, Boolean isShare) {

        BooleanBuilder builder = new BooleanBuilder();
        if (category != null) {
            builder.and(trade.category.eq(category));
        }

        if (isShare != null) {
            builder.and(trade.isShare.eq(isShare));
        }
        return queryFactory
                .selectFrom(trade)
                .where(builder)
                .fetch();
    }

    public List<Trade> get2(Category category, Boolean isShare,Long id) {
        return queryFactory
                .selectFrom(trade)
                .where(categoryEq(category), shareEp(isShare))
                .join(tradeImage.trade,trade)
                .fetchJoin()
                .fetch();

    }

    private BooleanExpression categoryEq(Category category) {
        return category != null ? trade.category.eq(category) : null;
    }

    private BooleanExpression shareEp(Boolean isShare) {
        return isShare != null ? trade.isShare.eq(isShare) : null;
    }
}
