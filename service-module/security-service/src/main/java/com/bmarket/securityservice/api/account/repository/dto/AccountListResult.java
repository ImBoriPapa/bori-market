package com.bmarket.securityservice.api.account.repository.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class AccountListResult extends EntityModel {

    private Long offSet;
    private Integer size;
    private Long totalCount;
    private List<AccountList> accountLists = new ArrayList<>();

    public AccountListResult(Long offSet,Integer size,Long totalCount,List<AccountList> accounts) {
        this.offSet = offSet;
        this.size = size;
        this.totalCount = totalCount;
        this.accountLists = accounts;
    }
}
