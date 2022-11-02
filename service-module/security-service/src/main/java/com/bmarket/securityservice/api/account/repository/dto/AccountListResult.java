package com.bmarket.securityservice.api.account.repository.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class AccountListResult extends EntityModel {

    private int currentPage;
    private int size;
    private List<AccountList> accountLists = new ArrayList<>();

    public AccountListResult(int offSet,int size,List<AccountList> accounts) {
        this.size = offSet;
        this.currentPage = size;
        this.accountLists = accounts;
    }
}
