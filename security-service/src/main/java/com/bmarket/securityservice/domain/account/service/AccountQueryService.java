package com.bmarket.securityservice.domain.account.service;

import com.bmarket.securityservice.domain.account.entity.Authority;
import com.bmarket.securityservice.domain.account.repository.AccountQueryRepository;
import com.bmarket.securityservice.domain.account.repository.dto.AccountList;
import com.bmarket.securityservice.domain.account.repository.dto.AccountListResult;
import com.bmarket.securityservice.domain.account.repository.dto.FindOneAccountResult;
import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import com.querydsl.core.NonUniqueResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
/**
 * 조회용 로직들은 AccountQueryRepository 에서 @Transactional(readOnly = true)을 설정해 놓아서 QueryService 트랜잭션 명시 x
 * exception 명시
 */
public class AccountQueryService {

    private final AccountQueryRepository queryRepository;

    /**
     * account id 로 계정 상세 조회
     *
     * @param accountId
     * @Return FindOneAccountResult
     */
    public FindOneAccountResult findAccountDetail(Long accountId) {
        try {
            return queryRepository.findOneAccount(accountId)
                    .orElseThrow(() -> new BasicException(ResponseStatus.NOT_FOUND_ACCOUNT));
        } catch (NonUniqueResultException e) {
            log.error("NonUniqueResultException 발생");
            throw new BasicException(ResponseStatus.NOT_FOUNT_REASON);
        }
    }

    /**
     * 계정 목록 조회
     * Pageable 을 사용한 offSet 기반 페이징
     * @param pageable, Authority or Null
     * @return AccountListResult
     */
    public AccountListResult findAccountList(Pageable pageable, Authority authority) {
        Page<AccountList> lists = queryRepository.findAccountListByPageable(pageable, authority);
        return new AccountListResult(lists);
    }


}
