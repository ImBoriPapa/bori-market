package com.bmarket.securityservice.service;

import com.bmarket.securityservice.api.account.controller.RequestAccountForm;
import com.bmarket.securityservice.api.account.controller.ResponseAccountForm;
import com.bmarket.securityservice.api.account.entity.Account;
import com.bmarket.securityservice.api.account.entity.Authority;
import com.bmarket.securityservice.api.account.repository.dto.AccountListResult;
import com.bmarket.securityservice.api.account.repository.dto.FindOneAccountResult;
import com.bmarket.securityservice.api.account.service.AccountCommandService;
import com.bmarket.securityservice.api.account.service.AccountQueryService;
import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.api.account.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
class AccountQueryServiceTest {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountQueryService accountQueryService;
    @Autowired
    AccountCommandService accountCommandService;

    @BeforeEach
    void beforeEach() {
        log.info("[TEST DATA INIT]");
        init();
        log.info("[TEST DATA INIT] FINISH");
    }

    @AfterEach
    void afterEach() {
        log.info("[TEST DATA DELETE]");
        accountRepository.deleteAll();
    }


    @Test
    @DisplayName("계정 상세 조회 테스트")
    void findAccountDetailTest() throws Exception {
        //given
        Long correctId = 1L;
        Long incorrectId = 1000L;
        //when
        log.info("[TEST QUERY START]");
        FindOneAccountResult accountDetail = accountQueryService.findAccountDetail(correctId);
        log.info("[TEST QUERY FINISH]");
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
         *  Account 총 101
         *  ROLL_USER 100개
         *  ROLL_ADMIN 1개
         *  조회시 정렬 기준은 accountId, 방향은 DESC
         */
        PageRequest request = PageRequest.of(0, 20);
        PageRequest request2 = PageRequest.of(2, 20);
        PageRequest request3 = PageRequest.of(0, 101);

        //when
        log.info("[TEST QUERY START]");
        AccountListResult findOnlyRequest = accountQueryService.findAccountList(request, null);
        AccountListResult findWithAuthority = accountQueryService.findAccountList(request, Authority.ADMIN);
        AccountListResult find3Page = accountQueryService.findAccountList(request2, Authority.USER);
        log.info("[TEST QUERY FINISH]");
        //then
        //검색 조건1 : page=0, size=20, authority= null
        assertThat(findOnlyRequest.getPageNumber()).isEqualTo(0);
        assertThat(findOnlyRequest.getSize()).isEqualTo(20);
        assertThat(findOnlyRequest.getTotalCount()).isEqualTo(101);
        assertThat(findOnlyRequest.getAccountLists().size()).isEqualTo(20);
        //검색 조건2 : page=0, size=20, authority= ROLL_AUTHORITY
        assertThat(findWithAuthority.getPageNumber()).isEqualTo(0);
        assertThat(findWithAuthority.getSize()).isEqualTo(20);
        assertThat(findWithAuthority.getTotalCount()).isEqualTo(1);
        assertThat(findWithAuthority.getAccountLists().size()).isEqualTo(1);
        //검색 조건3 : page=2, size=20, authority= ROLL_USER
        assertThat(find3Page.getPageNumber()).isEqualTo(2);
        assertThat(find3Page.getSize()).isEqualTo(20);
        assertThat(find3Page.getTotalCount()).isEqualTo(100);
        assertThat(find3Page.getAccountLists().size()).isEqualTo(20);

    }

    public void init() {
        ArrayList<Long> list = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            RequestAccountForm.CreateForm form = RequestAccountForm.CreateForm.builder()
                    .loginId("tester" + i)
                    .name("이테스트")
                    .nickname("브레드피트" + i)
                    .password("bread1234")
                    .email("bread" + i + "@bread.com")
                    .contact("010-2222-" + i)
                    .addressCode(1100)
                    .city("서울")
                    .district("종로구")
                    .town("암사동")
                    .build();
            ResponseAccountForm.ResponseSignupForm signupForm = accountCommandService.signUpProcessing(form);
            list.add(signupForm.getAccountId());
        }
    }

    public void deleteTestData() {
        accountRepository.deleteAll();
    }

}