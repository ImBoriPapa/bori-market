package com.bmarket.securityservice.api.account.controller.resultForm;

import com.bmarket.securityservice.api.account.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountListResult extends EntityModel {

    private int size;
    private Boolean hasPrevious;
    private Boolean hasNext;
    private int currentPage;
    private int nextPage;
    private int previousPage;
    private List<AccountList> accountLists = new ArrayList<>();

    public AccountListResult(Page<Account> page) {
        this.size = page.getSize();
        this.hasPrevious = page.hasPrevious();
        this.hasNext = page.hasNext();
        this.currentPage = page.getNumber();
        this.nextPage = page.getNumber()+1;
        this.previousPage = page.getNumber()-1;
        this.accountLists = page.getContent().stream().map(AccountList::new).collect(Collectors.toList());
    }
}
