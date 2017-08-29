package com.jaz.budgt;

import java.util.Date;

/**
 * Created by jaz on 8/11/17.
 */

public class Transaction {
    public enum TransactionType {
        INCOME, EXPENSE;
    }

    private int dollarAmount = 0;
    private int centAmount = 0;
    private TransactionType transactionType = TransactionType.EXPENSE;
    private int day = 0;
    private int month = 0;
    private int year = 0;
    private String category = "";
    private String paymentType = "";
    private String description = "";



    public Transaction(){

    }

    public Transaction(int dollars, int cents, int theDay, int theMonth, int theYear,
                       String category, String paymentType, String description ){
        this.dollarAmount = dollars;
        this.centAmount = cents;
        this.day = theDay;
        this.month = theMonth;
        this.year = theYear;
        this.category = category;
        this.paymentType = paymentType;
        this.description = description;
    }

    public String toString() {
        String string = "";
        if(this.transactionType == TransactionType.EXPENSE) {
            string += "Expense of ";
        } else {
            string += "Income of ";
        }
        string += "$" + dollarAmount + "." + centAmount
                + " for " + description + " (" + category + ")"
                + " on " + month + "/" + day + "/" + year
                + " through " + paymentType;
        return string;
    }

    public int getDollarAmount() {
        return dollarAmount;
    }

    public void setDollarAmount(int dollarAmount) {
        this.dollarAmount = dollarAmount;
    }

    public int getCentAmount() {
        return centAmount;
    }

    public void setCentAmount(int centAmount) {
        this.centAmount = centAmount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public void setDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
