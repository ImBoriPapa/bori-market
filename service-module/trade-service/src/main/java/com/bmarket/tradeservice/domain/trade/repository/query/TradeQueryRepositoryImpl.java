package com.bmarket.tradeservice.domain.trade.repository.query;

import com.bmarket.tradeservice.domain.trade.entity.Category;
import com.bmarket.tradeservice.domain.trade.entity.Trade;
import com.bmarket.tradeservice.domain.trade.entity.TradeStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.bmarket.tradeservice.domain.trade.entity.QTrade.trade;


@Repository
@RequiredArgsConstructor
public class TradeQueryRepositoryImpl implements TradeQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<TradeListDto> getTradeWithComplexCondition(Pageable pageable, SearchCondition searchCondition) {
        List<TradeListDto> list = queryFactory
                .select(
                        new QTradeListDto(
                                trade.id,
                                trade.title,
                                trade.townName,
                                trade.price,
                                trade.representativeImage,
                                trade.createdAt
                        )
                )
                .from(trade)
                .where(
                        categoryEq(searchCondition.getCategory()),
                        shareEp(searchCondition.getIsShare()),
                        offerEq(searchCondition.getIsOffer()),
                        statusEq(searchCondition.getStatus()),
                        addressSearchRange(searchCondition.getAddressSearchCondition(), searchCondition.getAddressCode())
                )
                .orderBy(trade.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        return new PageImpl<>(list, pageable, list.size());
    }

    private BooleanExpression addressSearchRange(AddressSearchCondition addressSearchCondition, int addressCode) {

        if (addressSearchCondition == AddressSearchCondition.JUST) {
            return trade.addressCode.eq(addressCode);
        }

        if (addressSearchCondition == AddressSearchCondition.FIVE) {
            return trade.addressCode.between(addressCode - 2, addressCode + 3);
        }

        if (addressSearchCondition == AddressSearchCondition.TEN) {
            return trade.addressCode.between(addressCode - 5, addressCode + 5);
        }

        throw new IllegalArgumentException("Address 검색 값은 필수 입니다.");
    }

    private BooleanExpression statusEq(TradeStatus status) {
        return status != null ? trade.status.eq(status) : trade.status.eq(TradeStatus.SALE);
    }

    private BooleanExpression offerEq(Boolean isOffer) {
        return isOffer != null ? trade.isOffer.eq(isOffer) : null;
    }

    private BooleanExpression categoryEq(Category category) {
        return category != null ? trade.category.eq(category) : null;
    }

    private BooleanExpression shareEp(Boolean isShare) {
        return isShare != null ? trade.isShare.eq(isShare) : null;
    }
}
