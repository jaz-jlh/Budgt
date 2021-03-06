package com.jaz.budgt;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by jaz on 8/11/17.
 */

public class Transaction {

    private int dollarAmount = 0;
    private int centAmount = 0;
    private boolean isExpense = true;
    private int day = 0;
    private int month = 0;
    private int year = 0;
    private String category;
    private String description = "";
    private String account;
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
            if(t1.isExpense()) t1amt = -t1amt;
            if(t2.isExpense()) t2amt = -t2amt;
            return t1amt - t2amt;
    }};

    public Transaction(int dollars, int cents, int theDay, int theMonth, int theYear,
                       String category, String account, String description){
        this.dollarAmount = dollars;
        this.centAmount = cents;
        this.day = theDay;
        this.month = theMonth;
        this.year = theYear;
        this.category = category;
        this.account = account;
        this.description = description;
        if(this.id == 0) {
            count++;
            this.id = count;
        }
    }

    public String toString() {
        String string = "";
        String pmt = " through ";
        if(this.isExpense) {
            string += "Expense of ";
            pmt = " with ";
        } else {
            string += "Income of ";
        }
        String zero = "";
        if(centAmount < 10) zero = "0";
        string += "$" + dollarAmount + "." + zero + centAmount
                + " for " + description + " (" + category + ")"
                + " on " + month + "/" + day + "/" + year;
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

    public String getStringAmount(boolean includeSign) {
        if(this.isExpense() && includeSign) {
            if (centAmount < 10) {
                return "-$" + dollarAmount + ".0" + centAmount;
            } else {
                return "-$" + dollarAmount + "." + centAmount;
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

    public boolean isExpense() {
        return isExpense;
    }

    public void setIsExpense(boolean isExpense) {
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

    public String toCSVString(String delimiter) {
        String csv = "";
        csv += getDateString(false) + delimiter;
        csv += getStringAmount(true) + delimiter;
        csv += description += delimiter;
        csv += category += delimiter;
        csv += account;
        return csv;
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

    public long calculateDayDifference(Date otherDate) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = simpleDateFormat.parse(this.getDay() + "/" + this.getMonth() + "/" + this.getYear());
        } catch(ParseException e){
            e.printStackTrace();
        }
        long delta = Math.abs(date.getTime() - otherDate.getTime());
        delta = delta / 86400000;
        TimeUnit.DAYS.convert(delta, TimeUnit.MILLISECONDS);
        Log.d("Transaction","Date delta: " + delta);
        return delta;
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

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }

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

    public String getAccount() { return account; }

    public void setAccount(String account) { this.account = account; }
}
