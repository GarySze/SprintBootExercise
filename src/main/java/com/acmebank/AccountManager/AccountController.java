package com.acmebank.AccountManager;

import java.util.List;

import com.acmebank.AccountManager.entity.Account;
import com.acmebank.AccountManager.entity.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController
{
    @Autowired
    IAccountService accountService;

    @RequestMapping(value = "/")
    public String hello() {
        return "Welcome to acme bank!";
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> list = accountService.getAll();
        return new ResponseEntity<List<Account>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value ="/transfer", method = RequestMethod.POST)
    public ResponseEntity<Object> transfer(@org.jetbrains.annotations.NotNull @RequestBody Transfer transfer) {
        accountService.transfer(transfer.srcAccId, transfer.destAccId, transfer.amount);
        return new ResponseEntity<>("Transfer is done successsfully", HttpStatus.CREATED);
    }
}
