package com.example.login;

public class Transaction {

    private String date;
    private int transactionId;
    private String amount;
    private String category;
    private String relatedAcc;
    private String sign;
    private String note;

    public Transaction(String date, int transactionId, String amount, String category, String relatedAcc, String sign, String note) {
        this.date = date;
        this.transactionId = transactionId;
        this.amount = amount;
        this.category = category;
        this.relatedAcc = relatedAcc;
        this.sign = sign;
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRelatedAcc() {
        return relatedAcc;
    }

    public void setRelatedAcc(String relatedAcc) {
        this.relatedAcc = relatedAcc;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

