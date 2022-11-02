package com.bmarket.securityservice.api.account.entity;

import com.bmarket.securityservice.api.account.repository.AccountRepository;
import com.bmarket.securityservice.api.address.Address;
import com.bmarket.securityservice.api.address.AddressRange;
import com.bmarket.securityservice.api.profile.entity.Profile;
import com.bmarket.securityservice.api.profile.repository.ProfileRepository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional
class AccountTest {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("Account 생성 테스트")
    void createAccount() throws Exception {
        //given
        Address address = Address.createAddress()
                .addressCode(1001)
                .city("city")
                .district("district")
                .town("town")
                .build();

        Profile profile = Profile.createProfile()
                .nickname("nickname")
                .profileImage("프로필이미지.jpg")
                .address(address).build();


        Account account = Account.createAccount()
                .loginId("login")
                .name("name")
                .password("1234")
                .email("email@email.com")
                .contact("010-1213-1213")
                .profile(profile)
                .build();
        Account savedAccount = accountRepository.save(account);
        //when
        Account findAccount = accountRepository.findById(savedAccount.getId())
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾지 못했습니다."));
        Profile findProfile = profileRepository.findById(findAccount.getProfile().getId())
                .orElseThrow(() -> new IllegalArgumentException("프로필을 찾지 못했습니다."));
        //then
        //계정 정보
        assertThat(findAccount.getId()).isEqualTo(savedAccount.getId());
        assertThat(findAccount.getClientId()).isEqualTo(savedAccount.getClientId());
        assertThat(findAccount.getLoginId()).isEqualTo(savedAccount.getLoginId());
        assertThat(findAccount.getName()).isEqualTo(savedAccount.getName());
        assertThat(findAccount.getPassword()).isEqualTo(savedAccount.getPassword());
        assertThat(findAccount.getEmail()).isEqualTo(savedAccount.getEmail());
        assertThat(findAccount.getContact()).isEqualTo(savedAccount.getContact());
        assertThat(findAccount.getAuthority()).isEqualTo(Authority.ROLL_USER);
        assertThat(findAccount.isLogin()).isFalse();
        assertThat(findAccount.getCreatedAt()).isEqualTo(savedAccount.getCreatedAt());
        assertThat(findAccount.getUpdatedAt()).isEqualTo(savedAccount.getUpdatedAt());
        assertThat(findAccount.getLastLoginTime()).isNull();
        //프로필 정보
        assertThat(findAccount.getProfile().getId()).isEqualTo(findProfile.getId());
        assertThat(findAccount.getProfile().getNickname()).isEqualTo("nickname");
        assertThat(findAccount.getProfile().getProfileImage()).isEqualTo("프로필이미지.jpg");
        assertThat(findAccount.getProfile().getAddressRange()).isEqualTo(AddressRange.JUST);
        assertThat(findAccount.getProfile().getAddress()).isSameAs(address);
    }

    @Test
    @DisplayName("SequentialUUID 조회 속도 테스트")
    void sequentialUUID() throws Exception {
        //given
        ArrayList<Account> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            Account account = Account.createAccount()
                    .loginId("login" + i)
                    .name("name")
                    .password("1234")
                    .email("email" + i + "@email.com")
                    .contact("010-1213-" + i)
                    .build();
            list.add(account);
        }
        List<Account> accounts = accountRepository.saveAll(list);

        ArrayList<Long> ids = new ArrayList<>();
        accounts.forEach(i -> ids.add(i.getId()));

        ArrayList<String> clientIds = new ArrayList<>();
        accounts.forEach(i -> clientIds.add(i.getClientId()));

        em.flush();
        em.clear();
        //when
        StopWatch st1 = new StopWatch();
        StopWatch st2 = new StopWatch();
        StopWatch st3 = new StopWatch();
        StopWatch st4 = new StopWatch();
        StopWatch st5 = new StopWatch();
        StopWatch st6 = new StopWatch();
        StopWatch st7 = new StopWatch();
        StopWatch st8 = new StopWatch();

        st1.start();
        accountRepository.findById(ids.get(0));
        st1.stop();

        st2.start();
        accountRepository.findById(ids.get(4999));
        st2.stop();

        st3.start();
        accountRepository.findById(ids.get(9999));
        st3.stop();

        st4.start();
        accountRepository.findByClientId(clientIds.get(0));
        st4.stop();

        st5.start();
        accountRepository.findByClientId(clientIds.get(4999));
        st5.stop();

        st6.start();
        accountRepository.findByClientId(clientIds.get(9999));
        st6.stop();

        PageRequest id = PageRequest.of(0, 20, Sort.Direction.DESC, "id");
        PageRequest clientId = PageRequest.of(0, 20, Sort.Direction.DESC, "clientId");

        st7.start();
        Page<Account> pageById = accountRepository.findAll(id);
        st7.stop();
        st8.start();
        Page<Account> pageByClientId = accountRepository.findAll(clientId);
        st8.stop();
        //then

        System.out.println("index  id로    0번째 찾는 속도 = " + st1.getTotalTimeMillis()+"ms");
        System.out.println("index  id로 4999번째 찾는 속도 = " + st2.getTotalTimeMillis()+"ms");
        System.out.println("index  id로 9999번째 찾는 속도 = " + st3.getTotalTimeMillis()+"ms");
        System.out.println("index  average= " + (st1.getTotalTimeMillis() + st2.getTotalTimeMillis() + st3.getTotalTimeMillis()) / 3+"ms");
        System.out.println("client id로    0번째 찾는 속도 = " + st4.getTotalTimeMillis()+"ms");
        System.out.println("client id로 4999번째 찾는 속도 = " + st5.getTotalTimeMillis()+"ms");
        System.out.println("client id로 9999번째 찾는 속도 = " + st6.getTotalTimeMillis()+"ms");
        System.out.println("client average= " + (st4.getTotalTimeMillis() + st5.getTotalTimeMillis() + st6.getTotalTimeMillis()) / 3+"ms");
        System.out.println("index id로 정렬헤서 페이징 조회 속도= " + st7.getTotalTimeMillis()+"ms");
        System.out.println("client id로 정렬헤서 페이징 조회 속도= " + st8.getTotalTimeMillis()+"ms");
    }
}