package com.acmebank.AccountManager;

import java.util.List;
import java.math.BigDecimal;

import com.acmebank.AccountManager.entity.Account;
import com.acmebank.AccountManager.exception.ClientException;
import com.acmebank.AccountManager.exception.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService implements IAccountService {
    @Autowired
    private JdbcTemplate jtm;

    @Override
    public List<Account> getAll() {
        try {
            String sql = "SELECT * FROM account";
            return jtm.query(sql, new BeanPropertyRowMapper<>(Account.class));
        }
        catch(Exception e){
            throw new ServerException("Get all accounts API: "+ e.getClass().getCanonicalName(), e);
        }
    }

    @Transactional
    public void transfer(String srcAccId, String destAccId, BigDecimal amount){
        try {
            // 1. validation before transfer
            List<Account> li = getAll();
            Account srcAcc= li.stream().filter(x-> x.getId().equals(srcAccId)).findFirst().orElse(null);
            if( srcAcc == null) {
                throw new ClientException("Source account ID not found!", null);
            }
            if (li.stream().anyMatch(x-> x.getId().equals(destAccId)) == false) {
                throw new ClientException("Destination account ID not found!", null);
            }
            if (srcAcc.getBalance().compareTo(amount) == -1) {
                throw new ClientException("Insufficient money in source account!", null);
            }

            // 2. execute the transfer
            String sqlDeduceAmountFromSrc = "update ACCOUNT set balance = balance - ? where id = ?";
            jtm.update(sqlDeduceAmountFromSrc, amount, srcAccId);

            String sqlAddAmountToDest = "update ACCOUNT set balance = balance + ? where id = ?";
            jtm.update(sqlAddAmountToDest, amount, destAccId);
        }
        catch(ClientException e) {
            throw e;
        }
        catch(Exception e){
            throw new ServerException("Money transfer API: "+ e.getClass().getCanonicalName(), e);
        }
    }
}