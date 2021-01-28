package com.acmebank.AccountManager.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {

    private String id;
    private BigDecimal balance;
	private String currency;
    
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Account account = (Account) o;
		return  Objects.equals(id, account.id) &&
				Objects.equals(balance, account.balance) &&
				Objects.equals(currency, account.currency);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, balance, currency);
	}

    @Override
    public String toString() {
        return "AccountEntity [id=" + id + ", balance=" + balance + ", currency=" + currency + "]";
    }
}