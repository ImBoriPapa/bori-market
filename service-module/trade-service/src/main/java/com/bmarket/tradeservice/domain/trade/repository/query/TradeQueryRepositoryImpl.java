package com.bmarket.tradeservice.domain.trade.repository.query;

import com.bmarket.tradeservice.domain.trade.entity.Category;
import com.bmarket.tradeservice.domain.trade.entity.TradeStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bmarket.tradeservice.domain.trade.entity.QTrade.trade;
import static com.bmarket.tradeservice.domain.trade.entity.QTradeImage.tradeImage;


@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TradeQueryRepositoryImpl implements TradeQueryRepository {

    private final JPAQueryFactory queryFactory;

    public TradeDetailDto getTradeDetail(Long id) {
        TradeDetailDto tradeDetailDto = queryFactory
                .select(new QTradeDetailDto(
                        trade.id,
                        trade.nickname,
                        trade.title,
                        trade.context,
                        trade.category,
                        trade.townName
                ))
                .from(trade)
                .where(trade.id.eq(id))
                .fetchOne();

        List<String> images = queryFactory
                .select(tradeImage.imageName)
                .from(tradeImage)
                .where(tradeImage.trade.id.eq(id))
                .fetch();

        tradeDetailDto.addImagePath(images);
        return tradeDetailDto;
    }

    @Override
    public ResponseResult<List<TradeListDto>> getTradeWithComplexCondition(int size, Long tradId, SearchCondition searchCondition) {

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
                .where(cursor(tradId),
                        categoryEq(searchCondition.getCategory()),
                        shareEp(searchCondition.getIsShare()),
                        offerEq(searchCondition.getIsOffer()),
                        statusEq(searchCondition.getStatus()),
                        addressSearchRange(searchCondition.getRange(), searchCondition.getAddressCode())
                )
                .orderBy(trade.id.desc())
                .limit(size + 1)
                .fetch();

        boolean hasNext = false;
        //list 11 > size 10
        //list.remove(10)4
        if (list.size() > size) {
            System.out.println("size=" + size);
            String title = list.remove(size).getTitle();
            System.out.println("target=" + title);
            hasNext = true;
        }

        return new ResponseResult<>(list.size(), hasNext, list);
    }

    private BooleanExpression cursor(Long tradeId) {
        return tradeId <= 0 ? null : trade.id.lt(tradeId);
    }

    private BooleanExpression addressSearchRange(AddressRange addressRange, int addressCode) {

        if (addressRange == AddressRange.JUST) {
            return trade.addressCode.eq(addressCode);
        }

        if (addressRange == AddressRange.FIVE) {
            return trade.addressCode.between(addressCode - 2, addressCode + 3);
        }

        if (addressRange == AddressRange.TEN) {
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
