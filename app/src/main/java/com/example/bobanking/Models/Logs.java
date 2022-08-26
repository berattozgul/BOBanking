package com.example.bobanking.Models;

public class Logs {
    private String toUser, fromUser, transactionDate;
    private int value;

    public Logs() {
    }

    public Logs(String toUser, String fromUser, String transactionDate,int value) {
        this.toUser = toUser;
        this.fromUser = fromUser;
        this.transactionDate = transactionDate;
        this.value=value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }
}
