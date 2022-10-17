package com.bmarket.securityservice.entity;


import com.bmarket.securityservice.domain.account.entity.Account;
import com.bmarket.securityservice.domain.account.entity.Authority;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.fasterxml.uuid.Generators;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;



import java.util.ArrayList;
import java.util.UUID;

@SpringBootTest
@Transactional
@Slf4j
class AccountTest {

    @Autowired
    AccountRepository accountRepository;

    @AfterEach
    public void afterEach(){
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("계정 생성 테스트")
    void create() throws Exception{
        //given
        Account account = Account.createAccount()
                .loginId("loginId")
                .name("tester")
                .password("test123")
                .build();
        //when
        Account save = accountRepository.save(account);
        Account findById = accountRepository.findById(save.getId()).get();
        //then
        Assertions.assertThat(findById.getId()).isEqualTo(save.getId());


    }

    @Test
    @DisplayName("UUID 생성 테스트 ")
    void generateUUID_Test() throws Exception{
        ArrayList<String> justUUID = new ArrayList<>();
        ArrayList<String> sequentialUUIDs = new ArrayList<>();
        //given
        for(int i=0; i<10; i++){
            String uuid = Generators.timeBasedGenerator().generate().toString();
            justUUID.add(uuid);
            sequentialUUIDs.add(getSequential().toString());
        }
        //when
        justUUID.stream().forEach(u->log.info("just UUID= {}, length= {}",u,u.length()));
        sequentialUUIDs.stream().forEach(u->log.info("c = {}, length= {}",u,u.length()));
        //then
    }

    @Test
    @DisplayName("sequential uuid unique 확인")
    void sequentialIsUnique() throws Exception{
        //given

        //when

        //then

    }

    private  StringBuffer getSequential() {
        UUID generate = Generators.timeBasedGenerator().generate();
        String[] uuidArr = generate.toString().split("-");
        String uuidStr = uuidArr[2] + uuidArr[1] + uuidArr[0] + uuidArr[4];
        StringBuffer sb = new StringBuffer(uuidStr);
        sb.insert(8, "-");
        sb.insert(13, "-");
        sb.insert(18, "-");
        sb.insert(23, "-");
        return sb;
    }


}