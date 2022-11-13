package com.bmarket.securityservice.api.account.repository;

import com.bmarket.securityservice.api.account.controller.RequestAccountForm;
import com.bmarket.securityservice.api.account.controller.ResponseAccountForm;
import com.bmarket.securityservice.api.account.entity.Account;
import com.bmarket.securityservice.api.account.entity.Authority;
import com.bmarket.securityservice.api.account.repository.dto.AccountList;
import com.bmarket.securityservice.api.account.repository.dto.FindOneAccountResult;
import com.bmarket.securityservice.api.account.repository.dto.UserDetailData;
import com.bmarket.securityservice.api.account.service.AccountCommandService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StopWatch;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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
    @Autowired
    EntityManager em;
    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void beforeEach() {
        log.info("[TEST DATA INIT] BEGAN");
        init();
        log.info("[TEST DATA INIT] FINISH");
    }

    @AfterEach
    void afterEach() {
        log.info("[TEST DATA DELETE]");
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("계정 단건 조회 테스트")
    void findOneAccountTest() throws Exception {
        //given
        Account tester = accountRepository.findByLoginId("tester1")
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
        log.info("[Test Query]");
        /**
         * ROLL_ADMIN 계정  1개
         * ROLL_USER 계정 100개
         * 총 101개 계정
         * 조회 순서는 DESC
         * 검색 조건은 pageNumber, size, Authority(ROLL_USER,ROLL_ADMIN,NULL)
         */
        PageRequest request1 = PageRequest.of(0, 20);
        PageRequest request2 = PageRequest.of(4, 20);
        PageRequest request3 = PageRequest.of(0, 20);
        PageRequest request4 = PageRequest.of(0, 20);
        //when
        Page<AccountList> result1 = queryRepository.findAccountListByPageable(request1, Authority.USER);
        Page<AccountList> result2 = queryRepository.findAccountListByPageable(request2, null);
        Page<AccountList> result3 = queryRepository.findAccountListByPageable(request3, Authority.ADMIN);
        accountCommandService.changeAuthority(1L, 50L, Authority.ADMIN);
        accountCommandService.changeAuthority(1L, 90L, Authority.ADMIN);
        Page<AccountList> result4 = queryRepository.findAccountListByPageable(request4, Authority.ADMIN);
        //then

        //검색 조건1 : pageNumber=0, size=20,Authority=ROLL_USER
        assertThat(result1.getNumber()).isEqualTo(0);
        assertThat(result1.getSize()).isEqualTo(20);
        assertThat(result1.getTotalPages()).isEqualTo(5);
        assertThat(result1.getContent().get(0).getAccountId()).isEqualTo(101);
        assertThat(result1.getContent().get(0).getLoginId()).isEqualTo("tester100");
        assertThat(result1.getContent().get(0).getEmail()).isEqualTo("bread100@bread.com");
        assertThat(result1.getContent().get(0).getAuthority()).isEqualTo(Authority.USER);
        assertThat(result1.getContent().get(0).getCreatedAt()).isNotNull();
        //검색 조건2 : pageNumber=4, size=20,Authority=NULL
        assertThat(result2.getNumber()).isEqualTo(4);
        assertThat(result2.getSize()).isEqualTo(20);
        assertThat(result2.getTotalPages()).isEqualTo(6);
        assertThat(result2.getContent().get(0).getAccountId()).isEqualTo(21);
        assertThat(result2.getContent().get(0).getLoginId()).isEqualTo("tester20");
        assertThat(result2.getContent().get(0).getEmail()).isEqualTo("bread20@bread.com");
        assertThat(result2.getContent().get(0).getAuthority()).isEqualTo(Authority.USER);
        assertThat(result2.getContent().get(0).getCreatedAt()).isNotNull();
        //검색 조건3 : pageNumber=0, size=20,Authority=ROLL_ADMIN
        assertThat(result3.getNumber()).isEqualTo(0);
        assertThat(result3.getSize()).isEqualTo(20);
        assertThat(result3.getTotalPages()).isEqualTo(1);
        assertThat(result3.getContent().get(0).getAccountId()).isEqualTo(1);
        assertThat(result3.getContent().get(0).getLoginId()).isEqualTo("manager");
        assertThat(result3.getContent().get(0).getEmail()).isEqualTo("manager@manager.com");
        assertThat(result3.getContent().get(0).getAuthority()).isEqualTo(Authority.ADMIN);
        assertThat(result3.getContent().get(0).getCreatedAt()).isNotNull();
        //검색 조건4 : pageNumber=0, size=20,Authority=ROLL_ADMIN
        assertThat(result4.getNumber()).isEqualTo(0);
        assertThat(result4.getSize()).isEqualTo(20);
        assertThat(result4.getTotalPages()).isEqualTo(1);
        assertThat(result4.getContent().get(0).getAccountId()).isEqualTo(90);
        assertThat(result4.getContent().get(0).getLoginId()).isEqualTo("tester89");
        assertThat(result4.getContent().get(0).getEmail()).isEqualTo("bread89@bread.com");
        assertThat(result4.getContent().get(0).getAuthority()).isEqualTo(Authority.ADMIN);
        assertThat(result4.getContent().get(0).getCreatedAt()).isNotNull();

    }

    @Test
    @DisplayName("UserDetailService 용 조회")
    void findAccountForLadUserTest() throws Exception {
        //given
        RequestAccountForm.CreateForm form = RequestAccountForm.CreateForm.builder()
                .loginId("testerA")
                .name("이테스트")
                .nickname("접근자")
                .password("1234fasfa")
                .email("mimo@gogi.com")
                .contact("010-2222-5311")
                .addressCode(1100)
                .city("서울")
                .district("종로구")
                .town("암사동")
                .build();
        ResponseAccountForm.ResponseSignupForm signupForm = accountCommandService.signUpProcessing(form);
        //when
        Account account = accountRepository.findById(signupForm.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을수 없습니다"));
        UserDetailData info = queryRepository.findAccountForLoadUser(account.getId())
                .orElseThrow(() -> new IllegalArgumentException("계정 정보를 찾을 수 없습니다"));
        //then
        assertThat(info.getAccountId()).isEqualTo(account.getId());
        assertThat(passwordEncoder.matches(form.getPassword(), info.getPassword())).isTrue();
        assertThat(info.getAuthority()).isEqualTo(Authority.USER);
    }

    @Test
    @DisplayName("트렌젝션 사용 속도 측정 (READ_ONLY = TRUE)")
    void useTransactionTest() throws Exception {
        //given
        ArrayList<Long> times = new ArrayList<>();
        StopWatch watch1 = new StopWatch();
        for (int i = 0; i < 100; i++) {
            watch1.start();
            queryRepository.useTransaction();
            watch1.stop();
            times.add(watch1.getTotalTimeNanos());
        }
        //when

        //then
        AtomicInteger i = new AtomicInteger();
        times.forEach(t ->
                        System.out.println("Using Transaction round= "+i.getAndIncrement()+", Time= "+t+"ns")
        );
        double asDouble = times.stream().mapToLong(m -> m).average().getAsDouble();
        System.out.println("Average="+asDouble+"ns");
    }

    @Test
    @DisplayName("트렌젝션 사용하지 않고 속도 측정 (트랜젝션 밖에서 읽기)")
    void donUseTransactionTest() throws Exception {
        //given

        ArrayList<Long> times2 = new ArrayList<>();
        StopWatch watch2 = new StopWatch();
        for (int i = 0; i < 100; i++) {
            watch2.start();
            queryRepository.donUseTransaction();
            watch2.stop();
            times2.add(watch2.getTotalTimeNanos());
        }
        //when
        AtomicInteger i = new AtomicInteger();
        //then
        times2.forEach(t ->
                        System.out.println("Don't Using Transaction round= "+i.getAndIncrement()+", Time= "+t+"ns")
        );
        double asDouble = times2.stream().mapToLong(m -> m).average().getAsDouble();
        System.out.println("Average="+asDouble+"ns");

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



}