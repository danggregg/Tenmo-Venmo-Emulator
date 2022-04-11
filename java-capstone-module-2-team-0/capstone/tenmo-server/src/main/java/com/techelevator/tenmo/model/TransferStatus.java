package com.techelevator.tenmo.model;

public class TransferStatus {
    private int statusIdForTransfer;
    private String descriptionForTransferStatus;

    public int getStatusIdForTransfer() {
        return statusIdForTransfer;
    }

    public void setStatusIdForTransfer(int statusIdForTransfer) {
        this.statusIdForTransfer = statusIdForTransfer;
    }

    public void setDescriptionForTransferStatus(String descriptionForTransferStatus) {
        this.descriptionForTransferStatus = descriptionForTransferStatus;
    }

    public String getDescriptionForTransferStatus() {
        return descriptionForTransferStatus;
    }
}
