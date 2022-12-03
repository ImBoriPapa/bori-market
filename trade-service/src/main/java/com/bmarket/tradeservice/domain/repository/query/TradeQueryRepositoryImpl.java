package com.bmarket.tradeservice.domain.repository.query;

import com.bmarket.tradeservice.domain.entity.TradeStatus;
import com.bmarket.tradeservice.domain.repository.query.dto.QTradeDetailDto;
import com.bmarket.tradeservice.domain.repository.query.dto.QTradeListResult;
import com.bmarket.tradeservice.domain.repository.query.dto.TradeDetailDto;
import com.bmarket.tradeservice.domain.entity.Category;

import com.bmarket.tradeservice.domain.repository.query.dto.TradeListResult;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.bmarket.tradeservice.domain.entity.QTrade.trade;
import static com.bmarket.tradeservice.domain.entity.QTradeImage.tradeImage;


@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TradeQueryRepositoryImpl implements TradeQueryRepository {
    private final JPAQueryFactory queryFactory;

    /**
     * 판매글 상세 조회
     */
    @Override
    public Optional<TradeDetailDto> findTradeDetailById(Long tradeId) {
        TradeDetailDto tradeDetailDto = queryFactory
                .select(new QTradeDetailDto(
                        trade.id,
                        trade.accountId,
                        trade.title,
                        trade.context,
                        trade.category,
                        trade.address,
                        trade.isShare,
                        trade.isOffer
                ))
                .from(trade)
                .where(trade.id.eq(tradeId))
                .fetchOne();

        List<String> images = queryFactory
                .select(tradeImage.imagePath)
                .from(tradeImage)
                .where(tradeImage.trade.id.eq(tradeId))
                .fetch();

        tradeDetailDto.addImagePath(images);

        return Optional.of(tradeDetailDto);
    }

    /**
     * 판매글 리스트 조회
     * SearchCondition : 검색조건
     */
    @Override
    public TradeListDto findTradeListWithCondition(int size, Long lastIndex, SearchCondition searchCondition) {

        List<TradeListResult> list = queryFactory
                .select(
                        new QTradeListResult(
                                trade.id,
                                trade.title,
                                trade.address.town,
                                trade.price,
                                trade.representativeImage,
                                trade.createdAt
                        )
                )
                .from(trade)
                .where(cursor(lastIndex),
                        categoryEq(searchCondition.getCategory()),
                        shareEp(searchCondition.getIsShare()),
                        offerEq(searchCondition.getIsOffer()),
                        statusEq(searchCondition.getStatus()),
                        addressSearchRange(searchCondition.getRange(), searchCondition.getAddressCode())
                )
                .orderBy(trade.id.desc())
                .limit(size + 1)
                .fetch();

        boolean hasNext = list.size() > size;

        return new TradeListDto(list.size(), hasNext, list);

    }

    private BooleanExpression cursor(Long tradeId) {
        return tradeId <= 0 ? null : trade.id.lt(tradeId);
    }

    private BooleanExpression addressSearchRange(AddressRange addressRange, int addressCode) {

        if (addressRange == AddressRange.ONLY) {
            return trade.address.addressCode.eq(addressCode);
        }

        if (addressRange == AddressRange.FIVE) {
            return trade.address.addressCode.between(addressCode - 2, addressCode + 3);
        }

        if (addressRange == AddressRange.TEN) {
            return trade.address.addressCode.between(addressCode - 5, addressCode + 5);
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
