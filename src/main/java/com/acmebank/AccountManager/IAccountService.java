package com.acmebank.AccountManager;

import com.acmebank.AccountManager.entity.Account;

import java.util.List;
import java.math.BigDecimal;

public interface  IAccountService {
    List<Account> getAll();
    void transfer(String srcAccId, String destAccId, BigDecimal amount);
}

