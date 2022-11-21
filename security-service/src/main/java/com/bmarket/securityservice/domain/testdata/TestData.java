package com.bmarket.securityservice.domain.testdata;

import com.bmarket.securityservice.domain.account.entity.Account;

import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.bmarket.securityservice.domain.address.Address;
import com.bmarket.securityservice.domain.profile.entity.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.bmarket.securityservice.domain.account.entity.Authority.*;
import static com.bmarket.securityservice.domain.testdata.TestAccountInfo.*;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TestData {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    private final EntityManager entityManager;

    public void initTestAccount(){

        resetIndex();
        Account account = Account.createAccount()
                .loginId("tester")
                .name("테스터")
                .password(passwordEncoder.encode("!@tester1234"))
                .email("test@test.com")
                .contact("42412421321")
                .build();
        log.info("[initTestAccount 용 일반 계정 생성]");
        accountRepository.save(account);
        log.info("[initTestAccount 용 일반 계정 생성]");
    }
    public void initAccount() {
        /**
         * 테스트용 일반 계정 1개
         *
         */
        log.info("AUTO INCREMENT 초기화");
        resetIndex();

        Address address = Address.createAddress()
                .addressCode(1100)
                .city("서울")
                .district("종로구")
                .town("암사동")
                .build();
        Profile profile = Profile.createProfile()
                .nickname("test")
                .profileImage("http://localhost:8095/frm/default.jpg")
                .address(address)
                .build();
        Account account = Account.createAccount()
                .loginId("tester")
                .name("테스터")
                .password(passwordEncoder.encode("!@tester1234"))
                .email("test@test.com")
                .contact("01011112222")
                .profile(profile)
                .build();

        log.info("[Test 용 일반 계정 생성]");
        accountRepository.save(account);
        log.info("[Test 용 일반 계정 생성 완료]");
    }

    public void initAccountList(int dataSize) {
        /**
         * Test Account
         * SUPER_ADMIN 1명
         * admin       2명
         * user        dataSize 명
         * 총           103명
         */
        log.info("AUTO INCREMENT 초기화");
        resetIndex();

        log.info("[Test 용 관리자 계정 생성]");
        Account admin1 = Account.createAdmin(TEST_ADMIN1_LOGIN_ID, "사장", passwordEncoder.encode(TEST_ADMIN_PASSWORD), "admin1@admin.com", "090111111111", SUPER_ADMIN);
        Account admin2 = Account.createAdmin(TEST_ADMIN2_LOGIN_ID, "책임자1", passwordEncoder.encode(TEST_ADMIN_PASSWORD), "admin2@admin.com", "090111112222", ADMIN);
        Account admin3 = Account.createAdmin(TEST_ADMIN3_LOGIN_ID, "책임자2", passwordEncoder.encode(TEST_ADMIN_PASSWORD), "admin3@admin.com", "090111113333", ADMIN);
        accountRepository.saveAll(List.of(admin1, admin2, admin3));
        log.info("[Test 용 관리자 계정 생성 완료]");

        ArrayList<Account> list = new ArrayList<>();
        for (int i = 1; i <= dataSize; i++) {
            Address address = Address.createAddress()
                    .addressCode(1100)
                    .city("서울")
                    .district("종로구")
                    .town("암사동")
                    .build();
            Profile profile = Profile.createProfile()
                    .nickname("test" + i)
                    .address(address)
                    .build();
            Account account = Account.createAccount()
                    .loginId("tester" + i)
                    .name("테스터" + i)
                    .password(passwordEncoder.encode("!@tester1234"))
                    .email("test" + i + "@test.com")
                    .contact("0101111" + i)
                    .profile(profile)
                    .build();
            list.add(account);
        }
        log.info("[Test 용 일반 계정 생성]");
        accountRepository.saveAll(list);
        log.info("[Test 용 일반 계정 생성 완료]");
    }


    public void clearAccount() {
        log.info("Test 용 데이터 삭제");
        accountRepository.deleteAll();
        log.info("Test 용 데이터 삭제 완료");
        log.info("AUTO INCREMENT 초기화");
        resetIndex();
    }

    private void resetIndex() {
        entityManager
                .createNativeQuery("ALTER TABLE account AUTO_INCREMENT= 0")
                .executeUpdate();
    }
}
