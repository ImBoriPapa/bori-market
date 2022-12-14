package com.bmarket.securityservice.domain.account.service;


import com.bmarket.securityservice.domain.account.entity.Account;
import com.bmarket.securityservice.domain.account.entity.Authority;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.bmarket.securityservice.domain.account.repository.dto.AccountListResult;
import com.bmarket.securityservice.domain.account.repository.dto.FindOneAccountResult;
import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.utils.testdata.TestDataProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("local")
@SpringBootTest
@Slf4j
class AccountQueryServiceTest {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountQueryService accountQueryService;
    @Autowired
    AccountCommandService accountCommandService;
    @Autowired
    TestDataProvider testDataProvider;

    @BeforeEach
    void beforeEach() throws IOException {
        testDataProvider.initAccountList(100);
    }

    @AfterEach
    void afterEach() throws IOException {
        testDataProvider.clearAccount();
    }


    @Test
    @DisplayName("계정 상세 조회 테스트")
    void findAccountDetailTest() throws Exception {
        //given
        Long correctId = 1L;
        Long incorrectId = 1000L;
        List<Account> all = accountRepository.findAll();
        all.forEach(m->log.info(" id ={}",m.getId()));
        all.forEach(m->log.info(" id ={}",m.getName()));
        //when
        log.info("[TEST QUERY START]==================================================================================");
        FindOneAccountResult accountDetail = accountQueryService.findAccountDetail(correctId);
        log.info("[TEST QUERY FINISH]=================================================================================");
        Account findAccount = accountRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을 수 없습니다"));

        //then
        assertThat(accountDetail.getAccountId()).isEqualTo(findAccount.getId());
        assertThat(accountDetail.getLoginId()).isEqualTo(findAccount.getLoginId());
        assertThat(accountDetail.getName()).isEqualTo(findAccount.getName());
        assertThat(accountDetail.getEmail()).isEqualTo(findAccount.getEmail());
        assertThat(accountDetail.getCreatedAt().compareTo(findAccount.getCreatedAt())).isEqualTo(0);
        assertThat(accountDetail.getUpdatedAt().compareTo(findAccount.getUpdatedAt())).isEqualTo(0);
        assertThatThrownBy(() ->
                accountQueryService.findAccountDetail(incorrectId)
        ).isExactlyInstanceOf(BasicException.class);
    }

    @Test
    @DisplayName("전체 조회 테스트")
    void searchAllTest() throws Exception {
        //given
        /**
         *  Account 총 103
         *  ROLL_USER 100개
         *  ROLL_ADMIN 2개
         *  조회시 정렬 기준은 accountId, 방향은 DESC
         */
        PageRequest request = PageRequest.of(0, 20);
        PageRequest request2 = PageRequest.of(2, 20);
        PageRequest request3 = PageRequest.of(0, 101);

        //when
        log.info("[TEST QUERY START]==================================================================================");
        AccountListResult findOnlyRequest = accountQueryService.findAccountList(request, null);
        AccountListResult findWithAuthority = accountQueryService.findAccountList(request, Authority.ADMIN);
        AccountListResult find3Page = accountQueryService.findAccountList(request2, Authority.USER);
        log.info("[TEST QUERY FINISH]=================================================================================");
        //then
        //검색 조건1 : page=0, size=20, authority= null
        assertThat(findOnlyRequest.getPageNumber()).isEqualTo(0);
        assertThat(findOnlyRequest.getSize()).isEqualTo(20);
        assertThat(findOnlyRequest.getTotalCount()).isEqualTo(103);
        assertThat(findOnlyRequest.getAccountLists().size()).isEqualTo(20);
        //검색 조건2 : page=0, size=20, authority= ROLL_AUTHORITY
        assertThat(findWithAuthority.getPageNumber()).isEqualTo(0);
        assertThat(findWithAuthority.getSize()).isEqualTo(20);
        assertThat(findWithAuthority.getTotalCount()).isEqualTo(2);
        assertThat(findWithAuthority.getAccountLists().size()).isEqualTo(2);
        //검색 조건3 : page=2, size=20, authority= ROLL_USER
        assertThat(find3Page.getPageNumber()).isEqualTo(2);
        assertThat(find3Page.getSize()).isEqualTo(20);
        assertThat(find3Page.getTotalCount()).isEqualTo(100);
        assertThat(find3Page.getAccountLists().size()).isEqualTo(20);
    }
}