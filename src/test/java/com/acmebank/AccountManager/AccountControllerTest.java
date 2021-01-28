package com.acmebank.AccountManager;

import com.acmebank.AccountManager.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {
    List<Account> accounts;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountService service;

    @BeforeEach
    public void setUp() {
        Account account = new Account();
        account.setId("1");
        account.setBalance(BigDecimal.valueOf(999));
        account.setCurrency("HKD");
        accounts = new ArrayList<Account>();
        accounts.add(account);
        account.setId("2");
        accounts.add(account);
    }

    @Test
    void Test_API_getAll() throws Exception {
        given(service.getAll()).willReturn(accounts);
        mvc.perform(get("/accounts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", anyOf( is(accounts.get(0).getId()), is(accounts.get(1).getId()))  ))
                ;
    }

    @Test
    void Test_API_transfer() throws Exception {
        String srcAccId="1";
        String destAccId="2";
        BigDecimal amount=BigDecimal.valueOf(3);
        doNothing().when(service).transfer(srcAccId, destAccId, amount);
        mvc.perform(post("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"srcAccId\" : \"" + srcAccId + "\", \"destAccId\" : \"" +destAccId+"\", \"amount\" : "+amount.toString()+ "}")  )
                .andExpect(status().isCreated())
        ;
    }

}