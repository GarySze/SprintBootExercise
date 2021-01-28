package com.acmebank.AccountManager;

import com.acmebank.AccountManager.entity.Account;
import com.acmebank.AccountManager.exception.ClientException;
import com.acmebank.AccountManager.exception.ServerException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AccountServiceIntegrationTest {

    @Autowired
    private JdbcTemplate jtm;

//    @Autowired
//    IAccountService service;

    @Test
    void TestCompanyName() {
        // Test case: Company name is acmebank
        String companyName = this.getClass().getCanonicalName().split("\\.")[1];
        assertEquals(companyName, "acmebank");
    }

    IAccountService CreateService() {
        AccountService service = new AccountService();
        ReflectionTestUtils.setField(service, "jtm", jtm);
        return service;
    }

    @Test
    void TestInitialStatusOfAccounts() {
        IAccountService service = CreateService();
        List<Account> accounts = service.getAll();

        // Test case: There are 2 accounts
        assertEquals(accounts.size(), 2);

        // Test case: Account 12345678 and 888888888 exists and the balance is 1000000
        assertEquals(accounts.stream().anyMatch(x-> x.getId().equals("12345678") &&
                                                    x.getBalance().compareTo(BigDecimal.valueOf(1000000))==0 ), true);
        assertEquals(accounts.stream().anyMatch(x-> x.getId().equals("88888888") &&
                                                    x.getBalance().compareTo(BigDecimal.valueOf(1000000))==0 ), true);
    }

    @Test
    void TestTransferNormal() {
        IAccountService service = CreateService();

        // Test case: After transfer, source account deduced by 100000, same amount added to destination account
        List<Account> accounts = service.getAll();
        Account srcAccBeforeTrf = accounts.get(0);
        Account destAccBeforeTrf = accounts.get(1);
        BigDecimal amount = BigDecimal.valueOf(10000);
        service.transfer(srcAccBeforeTrf.getId(), destAccBeforeTrf.getId(), amount);
        List<Account> accountsAferTrf = service.getAll();
        assertEquals(accountsAferTrf.stream().anyMatch(
                        x-> x.getId().equals( srcAccBeforeTrf.getId() ) &&
                        x.getBalance().compareTo( srcAccBeforeTrf.getBalance().subtract(amount) )==0 ), true);
        assertEquals(accountsAferTrf.stream().anyMatch(
                        x-> x.getId().equals( destAccBeforeTrf.getId() ) &&
                        x.getBalance().compareTo( destAccBeforeTrf.getBalance().add(amount) )==0 ), true);
    }

    @Test
    void TestTransferBalanceZero() {
        IAccountService service = CreateService();

        // Test case: Transfer success if src account balance will become zero after transfer
        List<Account> accounts = service.getAll();
        Account srcAccBeforeTrf = accounts.get(1);
        Account destAccBeforeTrf = accounts.get(0);
        BigDecimal amount = destAccBeforeTrf.getBalance();

        service.transfer(srcAccBeforeTrf.getId(), destAccBeforeTrf.getId(), amount);
        List<Account> accountsAferTrf = service.getAll();
        assertEquals(accountsAferTrf.stream().anyMatch(
                        x-> x.getId().equals( srcAccBeforeTrf.getId() ) &&
                        x.getBalance().compareTo( BigDecimal.valueOf(0) )==0 ), true);
        assertEquals(accountsAferTrf.stream().anyMatch(
                        x-> x.getId().equals( destAccBeforeTrf.getId() ) &&
                        x.getBalance().compareTo( destAccBeforeTrf.getBalance().add(amount) )==0 ), true);

        // Test case: Transfer fail if src account balance will become negative after transfer
        Throwable e = assertThrows(ClientException.class, ()-> {
            service.transfer(srcAccBeforeTrf.getId(), destAccBeforeTrf.getId(), BigDecimal.valueOf(100));
        });
        assertEquals(e.getMessage(), "Insufficient money in source account!");
    }

    @Test
    void TestClientSideException() {
        IAccountService service = CreateService();
        List<Account> accounts = service.getAll();

        // Test case: Validate request: source account ID
        Throwable e1 = assertThrows(ClientException.class, ()-> {
            service.transfer("invalid src id", accounts.get(0).getId(), BigDecimal.valueOf(100));
        });
        assertEquals(e1.getMessage(), "Source account ID not found!");

        // Test case: Validate request: destination account ID
        Throwable e2 = assertThrows(ClientException.class, ()-> {
            service.transfer(accounts.get(0).getId(), "invalid dest id", BigDecimal.valueOf(100));
        });
        assertEquals(e2.getMessage(), "Destination account ID not found!");
    }

    @Test
    void TestServerSideException() {
        AccountService service = new AccountService();  // Note: AccountService.jtm is null after creation if run under JUnit test

        // Test case: Should throw server exception if db access failure
        assertThrows(ServerException.class, () -> {
            service.getAll();
        });
    }
}