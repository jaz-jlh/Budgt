package com.jaz.budgt.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.util.Log;

import com.jaz.budgt.database.DateTypeConverter;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by jaz on 8/11/17.
 */

@Entity(tableName = "transactions")
@TypeConverters(DateTypeConverter.class)
public class Transaction {

    // Fields

    @PrimaryKey(autoGenerate = true)
    private int uid;
    @ColumnInfo(name="dollar_amount")
    private int dollarAmount = 0;
    @ColumnInfo(name="cent_amount")
    private int centAmount = 0;
    @ColumnInfo(name="is_expense")
    private boolean isExpense = true;
    @ColumnInfo(name="date")
    private Date date;
    @ColumnInfo(name="category")
    private String category = "";
    @ColumnInfo(name="description")
    private String description = "";
    @ColumnInfo(name = "account")
    private String account = "";


    // Constructors

    public Transaction(){
    }

    @Ignore
    public Transaction(int dollars, int cents, Date date, String category, String account, String description, boolean isExpense){
        this.dollarAmount = dollars;
        this.centAmount = cents;
        this.date = date;
        this.category = category;
        this.account = account;
        this.description = description;
        this.isExpense = isExpense;
    }


    // Custom Class for calculations, string values etc

    public String toString() {
        String string = "";
        String acct = " to ";
        if(this.isExpense) {
            string += "Expense of ";
            acct = " with ";
        } else {
            string += "Income of ";
        }
        String zero = "";
        if(centAmount < 10) zero = "0";
        string += "$" + dollarAmount + "." + zero + centAmount
                + acct + this.getAccount()
                + " for " + description + " (" + category + ")"
                + " on " + this.getDateString("MM/dd/yyyy");
        return string;
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

    public String getDateString(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(this.getDate());
    }

    public String toCSVString(String delimiter) {
        String csv = "";
        csv += getDateString("MM/dd/yyyy") + delimiter;
        csv += getStringAmount(true) + delimiter;
        csv += description += delimiter;
        csv += category += delimiter;
        csv += account;
        return csv;
    }

    public long calculateDayDifference(Date otherDate) {
        long delta = Math.abs(this.getDate().getTime() - otherDate.getTime());
        delta = delta / 86400000;
        TimeUnit.DAYS.convert(delta, TimeUnit.MILLISECONDS);
        Log.d("Transaction","Date delta: " + delta);
        return delta;
    }


    // Comparators

    public static Comparator<Transaction> transactionDateComparator = new Comparator<Transaction>() {
        public int compare(Transaction t1, Transaction t2) {
            long diff = t1.getDate().getTime() - t2.getDate().getTime();
            return diff < 0 ? 1 : -1;
        }
    };

    public static Comparator<Transaction> transactionAmountComparator = new Comparator<Transaction>() {
        public int compare(Transaction t1, Transaction t2) {
            int t1amt = (int)(t1.getAmount()*100);
            int t2amt = (int)(t2.getAmount()*100);
            if(t1.isExpense()) t1amt = -t1amt;
            if(t2.isExpense()) t2amt = -t2amt;
            return t1amt - t2amt;
        }};


    // Auto Generated Getters & Setters

    public int getUid() { return uid; }
    public void setUid(int uid) { this.uid = uid; }

    public int getDollarAmount() { return dollarAmount; }
    public void setDollarAmount(int dollarAmount) { this.dollarAmount = dollarAmount; }

    public int getCentAmount() { return centAmount; }
    public void setCentAmount(int centAmount) { this.centAmount = centAmount; }

    public boolean isExpense() { return isExpense; }
    public void setExpense(boolean expense) { isExpense = expense; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }
}
