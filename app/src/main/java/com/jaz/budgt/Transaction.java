package com.jaz.budgt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by jaz on 8/11/17.
 */

public class Transaction {

    private int dollarAmount = 0;
    private int centAmount = 0;
    private int isExpense = 1;
    //todo make this a boolean
    private int day = 0;
    private int month = 0;
    private int year = 0;
    private Category category;
    private String paymentType = "";
    private String description = "";
    private long id = 0;
    private static long count = 0;

    public Transaction(){
        count++;
        this.id = count;
    }

    public static Comparator<Transaction> transactionDateComparator = new Comparator<Transaction>() {
        public int compare(Transaction t1, Transaction t2) {
            long diff = t1.getDate().getTime() - t2.getDate().getTime();
            return diff < 0 ? 1 : -1;
        }};

    public static Comparator<Transaction> transactionAmountComparator = new Comparator<Transaction>() {
        public int compare(Transaction t1, Transaction t2) {
            int t1amt = (int)(t1.getAmount()*100);
            int t2amt = (int)(t2.getAmount()*100);
            if(t1.isExpense()==1) t1amt = -t1amt;
            if(t2.isExpense()==1) t2amt = -t2amt;
            return t1amt - t2amt;
    }};

    public Transaction(int dollars, int cents, int theDay, int theMonth, int theYear,
                       Category category, String paymentType, String description ){
        this.dollarAmount = dollars;
        this.centAmount = cents;
        this.day = theDay;
        this.month = theMonth;
        this.year = theYear;
        this.category = category;
        this.paymentType = paymentType;
        this.description = description;
        if(this.id == 0) {
            count++;
            this.id = count;
        }
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
        trans[5] = this.category.toString();
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
        this.category.setName(parts[5]);
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

    public String getStringAmount() {
        if(this.isExpense() == 1) {
            if (centAmount < 10) {
                return "$" + dollarAmount + ".0" + centAmount;
            } else {
                return "$" + dollarAmount + "." + centAmount;
            }
        } else {
            if (centAmount < 10) {
                return "$" + dollarAmount + ".0" + centAmount;
            } else {
                return "$" + dollarAmount + "." + centAmount;
            }
        }
        //todo clean this crap up
    }

    public double getAmount() {
        return (double) dollarAmount + (double) centAmount / 100.;
    }

    public int isExpense() {
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

    public String getDateString(boolean shortDate) {
        String date = "";
        date += month + "/" + day + "/";
        if(shortDate) {
            date += Integer.toString(year - 2000);
        } else {
            date += year;
        }
        return date;
    }

    public Date getDate() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = simpleDateFormat.parse(this.getDay() + "/" + this.getMonth() + "/" + this.getYear());
        } catch(ParseException e){
            e.printStackTrace();
        }
        return date;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public Category getCategory() { return category; }

    public void setCategory(Category category) { this.category = category; }

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
