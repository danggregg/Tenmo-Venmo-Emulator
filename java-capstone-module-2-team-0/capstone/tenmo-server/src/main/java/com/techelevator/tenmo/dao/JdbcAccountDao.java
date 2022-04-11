package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {
    private JdbcTemplate jdbcTemplate;
public JdbcAccountDao(DataSource dataSource) {
    this.jdbcTemplate =new JdbcTemplate(dataSource);
}

    @Override
    public BigDecimal getBalance(int userid) {
//        Integer balance;

        BigDecimal balance = null;
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userid);

//        SqlRowSet results = null;
//        BigDecimal balance = null;
//        results = jdbcTemplate.queryForRowSet(sql, userid);
        return balance;
    }

    //TODO FindIdByUsername not initiated.  in JDBCUserDao

    @Override
    public BigDecimal depositToBalance(BigDecimal transferAmount, int userId) {
//        BigDecimal balance = BigDecimal.valueOf(0);
//        int idForDeposit = findIdByUsername(username);
        BigDecimal updatedBalance = getBalance(userId).add(transferAmount);
        // Display new balance
        String sql = "UPDATE account SET balance = ? WHERE user_id = ?";

        return updatedBalance;
    }
    @Override
    public BigDecimal withdrawalFromBalance(BigDecimal transferAmount, int userId) {
//        BigDecimal balance = BigDecimal.valueOf(0);
//        int idForWithdrawal = findIdByUsername(username);
        // TODO Username.getBalance needs to be initialized
        BigDecimal updatedBalance = getBalance(userId).subtract(transferAmount);
        // Display new Balance
        String sql = "UPDATE account SET balance = ? Where user_id =?";

        return updatedBalance;
    }
    @Override
    public String completeTransfer (int fromUserId, int toUserId, BigDecimal transferAmount) {
    withdrawalFromBalance(transferAmount,fromUserId);
    depositToBalance(transferAmount,toUserId);
    return "Transfer Completed";
    }

    @Override
    public Account findAccountByUserId(int accountId) {
        String sql = "SELECT account_id, account.user_id, balance from account" +
                " JOIN tenmo_user ON account.user_id = tenmo_user.user_id where tenmo_user.user_Id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId);
        if (rowSet.next()) {
            return mapRowToAccount(rowSet);
        }
        throw new UsernameNotFoundException("Account ID " + accountId + " was not found.");
    }


    private Account mapRowToAccount(SqlRowSet stupidDumbRowStuff){
        Account account = new Account();
        account.setAccountId(stupidDumbRowStuff.getInt("account_id"));
        account.setBalance(stupidDumbRowStuff.getBigDecimal("balance"));
        account.setUserId(stupidDumbRowStuff.getInt("user_id"));

        return account;


    }
}




