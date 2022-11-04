package com.bmarket.securityservice.api.account.repository.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;


import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class AccountListResult extends EntityModel {

    private Integer pageNumber;
    private Integer size;
    private Long totalCount;
    private List<AccountList> accountLists = new ArrayList<>();

    public AccountListResult(Page<AccountList> accountLists) {
        this.pageNumber = accountLists.getNumber();
        this.size = accountLists.getSize();
        this.totalCount = accountLists.getTotalElements();
        this.accountLists.addAll(accountLists.getContent());
    }
}
