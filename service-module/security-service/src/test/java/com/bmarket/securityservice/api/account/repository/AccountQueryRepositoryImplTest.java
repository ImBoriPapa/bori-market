package com.bmarket.securityservice.api.account.repository;

import com.bmarket.securityservice.api.account.controller.requestForm.RequestSignUpForm;
import com.bmarket.securityservice.api.account.controller.resultForm.SignupResult;
import com.bmarket.securityservice.api.account.entity.Account;
import com.bmarket.securityservice.api.account.repository.dto.FindOneAccountResult;
import com.bmarket.securityservice.api.account.service.AccountCommandService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
class AccountQueryRepositoryImplTest {

    @Autowired
    AccountQueryRepository queryRepository;
    @Autowired
    AccountCommandService accountCommandService;
    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void beforeEach() {
        log.info("============================여기서 부터 before ====================");
        RequestSignUpForm form = RequestSignUpForm.builder()
                .loginId("tester")
                .name("이테스트")
                .nickname("브레드피트")
                .password("bread1234")
                .email("bread@bread.com")
                .contact("010-2222-1234")
                .addressCode(1100)
                .city("서울")
                .district("종로구")
                .town("암사동")
                .build();
        accountCommandService.signUpProcessing(form);
    }

    @AfterEach
    void afterEach() {
        log.info("============================여기서 부터 after ====================");
        Optional<Account> tester = accountRepository.findByLoginId("tester");
        accountCommandService.deleteAccount(tester.get().getId());
    }

    @Test
    @DisplayName("계정 단건 조회 테스트")
    void findOneAccountTest() throws Exception {
        //given
        Account tester = accountRepository.findByLoginId("tester")
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을수 없습니다."));
        log.info("============================여기서 부터 query ====================");
        //when
        FindOneAccountResult result = queryRepository.findOneAccount(tester.getId())
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을수 없습니다."));

        Optional<FindOneAccountResult> emptyResult = queryRepository.findOneAccount(1000L);
        //then
        assertThat(result.getAccountId()).isEqualTo(tester.getId());
        assertThat(result.getLoginId()).isEqualTo(tester.getLoginId());
        assertThat(result.getName()).isEqualTo(tester.getName());
        assertThat(result.getEmail()).isEqualTo(tester.getEmail());
        assertThat(result.getContact()).isEqualTo(tester.getContact());
        assertThat(result.getCreatedAt().compareTo(tester.getCreatedAt())).isEqualTo(0);
        assertThat(result.getUpdatedAt().compareTo(tester.getUpdatedAt())).isEqualTo(0);

        assertThat(emptyResult.isEmpty()).isTrue();

    }

    @Test
    @DisplayName("계정 리스트 조회")
    void findAccountListTest() throws Exception {
        //given
        List<Long> init = init();

        //when

        //then
        delete(init);

    }
    public List<Long> init() {
        ArrayList<Long> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            RequestSignUpForm form = RequestSignUpForm.builder()
                    .loginId("tester" + i)
                    .name("이테스트")
                    .nickname("브레드피트" + i)
                    .password("bread1234")
                    .email("bread" + i + "@bread.com")
                    .contact("010-2222-"+i)
                    .addressCode(1100)
                    .city("서울")
                    .district("종로구")
                    .town("암사동")
                    .build();
            SignupResult result = accountCommandService.signUpProcessing(form);
            list.add(result.getAccountId());
        }
        return list;
    }

    public void delete(List<Long> ids){
        accountRepository.deleteAllById(ids);
    }
}