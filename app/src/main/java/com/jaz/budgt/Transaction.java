package com.jaz.budgt;

/**
 * Created by jaz on 8/11/17.
 */

public class Transaction {

    private int dollarAmount = 0;
    private int centAmount = 0;
    private int isExpense = 1;
    private int day = 0;
    private int month = 0;
    private int year = 0;
    private String category = "";
    private String paymentType = "";
    private String description = "";

    public Transaction(){    }

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
        String pmt = " through ";
        if(this.isExpense==1) {
            string += "Expense of ";
            pmt = " with ";
        } else {
            string += "Income of ";
        }
        String zero = "";
        if(centAmount < 10) zero = "0";
        string += "$" + dollarAmount + "." + zero + centAmount
                + " for " + description + " (" + category + ")"
                + " on " + month + "/" + day + "/" + year
                + pmt + paymentType;
        return string;
    }

    public String[] toStringArray() {
        String[] trans = new String[9];
        trans[0] = Integer.toString(this.dollarAmount);
        trans[1] = Integer.toString(this.centAmount);
        trans[2] = Integer.toString(this.day);
        trans[3] = Integer.toString(this.month);
        trans[4] = Integer.toString(this.year);
        trans[5] = this.category;
        trans[6] = this.paymentType;
        trans[7] = this.description;
        trans[8] = Integer.toString(this.isExpense);
        return trans;
    }

    public Transaction(String[] parts) {
        this.dollarAmount = Integer.parseInt(parts[0]);
        this.centAmount = Integer.parseInt(parts[1]);
        this.day = Integer.parseInt(parts[2]);
        this.month = Integer.parseInt(parts[3]);
        this.year = Integer.parseInt(parts[4]);
        this.category = parts[5];
        this.paymentType = parts[6];
        this.description = parts[7];
        this.isExpense = Integer.parseInt(parts[8]);
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

    public int getIsExpense() {
        return isExpense;
    }

    public void setIsExpense(int isExpense) {
        this.isExpense = isExpense;
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
