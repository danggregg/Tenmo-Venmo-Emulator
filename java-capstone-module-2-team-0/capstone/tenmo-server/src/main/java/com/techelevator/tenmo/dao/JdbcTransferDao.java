package com.techelevator.tenmo.dao;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    private JdbcTemplate jdbcTemplate;
    UserDao userDao;

    public JdbcTransferDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public Transfer getTransfersById(int transferId) {
        Transfer transfer = new Transfer();
        String sql = "SELECT * " +
                "FROM transfer " +
                "WHERE transfer_id = ?";

        SqlRowSet transferList = jdbcTemplate.queryForRowSet(sql, transferId);
        if (transferList.next()) {
            transfer = mapRowToTransfer(transferList);

        }
        return transfer;
    }

    @Override
    public List<Transfer> getListOfAllTransfers(int userId) {
        List<Transfer> listOfTransfers = new ArrayList<>();

        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer " +
                "JOIN account ON account.account_id = transfer.account_from " +
                "WHERE account.user_id = ?";
        SqlRowSet fullTransferList = jdbcTemplate.queryForRowSet(sql, userId);
        while (fullTransferList.next()) {
//            Transfer transfers = mapRowToTransfer(fullTransferList);
            listOfTransfers.add(mapRowToTransfer(fullTransferList));
        }
        return listOfTransfers;
    }

    @Override
    public boolean sendTEBucks(int userFrom, int userTo, Transfer transfer) {

        boolean didItWork = false;
//        if (userFrom == userTo) {
//            return didItWork;
//        }
//        if ((transfer.getAmount().compareTo(userDao.getBalance(userFrom))) <= 0) {


            String sql = "BEGIN; " +
                    "INSERT INTO transfer " +
                    "(transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                    "VALUES (DEFAULT, 2, 2, ?, ?, ?); " +
                    "UPDATE account " +
                    "SET balance = balance - ? " +
                    "WHERE account_id = ?; " +
                    "UPDATE account " +
                    "SET balance = balance + ? " +
                    "WHERE account_id = ?; " +
                    "COMMIT; ";
            int num = jdbcTemplate.update(sql,
                    transfer.getAccountFrom(),
                    transfer.getAccountTo(),
                    transfer.getAmount(),
                    transfer.getAmount(),
                    userFrom,
                    transfer.getAmount(),
                    userTo);
            if (num == 1) {
                didItWork = true;
            }
//        }
        return didItWork;
    }


//    @ResponseStatus(value = HttpStatus.ACCEPTED)
//    private String transfer(int sendingFromUserId, int sendingToUserId, BigDecimal transferAmount) {
//        if (sendingFromUserId == sendingToUserId) {
//
//            return "Invalid User.  Select Valid User ID";
//
//        }if ((transferAmount.compareTo(getBalance(sendingFromUserId))) >= 0)
//        {
//            String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount)"
//                    + "VALUES 2,2,?,?,?";
//            jdbcTemplate.update(sql, sendingFromUserId, sendingToUserId, transferAmount);
//            return "Transfer Completed";}
//
//        return "Insufficient Funds";
//    }


    private Transfer mapRowToTransfer(SqlRowSet stupidDumbSqlRowSetResults) {


        Transfer transfer = new Transfer();
        transfer.setTransferId(stupidDumbSqlRowSetResults.getInt("transfer_id"));
        transfer.setTransferTypeId(stupidDumbSqlRowSetResults.getInt("transfer_type_id"));
        transfer.setTransferStatusId(stupidDumbSqlRowSetResults.getInt("transfer_status_id"));
        transfer.setAccountFrom(stupidDumbSqlRowSetResults.getInt("account_from"));
        transfer.setAccountTo(stupidDumbSqlRowSetResults.getInt("account_to"));
        transfer.setAmount(stupidDumbSqlRowSetResults.getBigDecimal("amount"));


//        transfer.setTransferType(stupidDumbSqlRowSetResults.getString("transfer_type_desc"));
//        transfer.setTransferStatus(stupidDumbSqlRowSetResults.getString("transfer_status_desc"));
//        transfer.setUserFrom(stupidDumbSqlRowSetResults.getString("user_from"));
//        transfer.setUserTo(stupidDumbSqlRowSetResults.getString("user_to"));
        return transfer;


    }

}
