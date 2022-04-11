package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.List;


public interface TransferDao {
    public Transfer getTransfersById(int transferId);

    public List<Transfer> getListOfAllTransfers(int userId);

    boolean sendTEBucks(int userFrom, int userTo, Transfer transfer);
}
