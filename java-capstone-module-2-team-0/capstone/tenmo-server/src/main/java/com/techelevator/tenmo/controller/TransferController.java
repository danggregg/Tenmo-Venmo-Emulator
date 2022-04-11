package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;


@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {
    private UserDao userDao;
    private Transfer transfer;
    private TransferDao transferDao;
    private AccountDao accountDao;

    public TransferController(TransferDao transfer, UserDao user, AccountDao account) {
        this.transferDao = transfer;
        this.userDao = user;
        this.accountDao = account;
    }


    // authentication
    @RequestMapping(path = "transfer/{userid}", method = RequestMethod.GET)
    public Transfer getTransfer(@PathVariable int userid) {
        Transfer transfer = transferDao.getTransfersById(userid);
        return transfer;
    }

    @RequestMapping(path = "account/transfer/{userid}", method = RequestMethod.GET)
    public List<Transfer> listOfTransfers(@PathVariable int userid) {
        List<Transfer> response = transferDao.getListOfAllTransfers(userid);
        return response;
    }


    @RequestMapping(path = "transfer/{sendingFromUserId}/{sendingToUserId}/{transferAmount}", method = RequestMethod.POST)
//    public String transfer(@PathVariable Integer sendingFromUserId, Integer sendingToUserId, BigDecimal transferAmount)
    public String transfer(@RequestBody @PathVariable int sendingFromUserId, @PathVariable int sendingToUserId,
                           @PathVariable BigDecimal transferAmount) {
        String response = userDao.transfer(sendingFromUserId, sendingToUserId, transferAmount);
        return response;
    }

    @RequestMapping(path = "transfer/{sendingFromUserId}/{sendingToUserId}/{balance}", method = RequestMethod.POST)
//    public String completeTransfer(@PathVariable int sendingFromUserId, @PathVariable int sendingToUserId,
//                                   @PathVariable BigDecimal transferAmount)
    public String completeTransfer(@RequestBody int sendingFromUserId, int sendingToUserId,
                                   BigDecimal transferAmount) {
        String response = accountDao.completeTransfer(sendingFromUserId, sendingToUserId, transferAmount);
        return response;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "transfer/{fromId}/{toId}", method = RequestMethod.POST)
    public boolean sendBucks(@PathVariable int fromId, @PathVariable int toId,
                             @Valid @RequestBody Transfer transfer) {
        return transferDao.sendTEBucks(fromId, toId, transfer);
    }

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



