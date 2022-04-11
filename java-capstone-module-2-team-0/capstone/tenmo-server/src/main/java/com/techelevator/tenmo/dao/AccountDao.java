package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {
    BigDecimal getBalance(int userid);


    Account findAccountByUserId(int accountId);

    public BigDecimal depositToBalance(BigDecimal transferAmount, int userId);
    public BigDecimal withdrawalFromBalance(BigDecimal transferAmount, int userId);
    public String completeTransfer(int fromUserId, int toUserId, BigDecimal transferAmount);




}
