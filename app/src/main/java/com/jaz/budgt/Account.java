package com.jaz.budgt;

import java.util.ArrayList;

/**
 * Created by jaz on 9/30/17.
 */

public class Account {

    private ArrayList<Transaction> transactions = new ArrayList<>(0);
    private int totalDollarValue = 0;
    private int totalCentValue = 0;
    private String name;

    public Account() { this.name = "Unnamed"; }

    public Account(String n) { this.name = n; }


    public void addTransaction(Transaction t) {
        if(!transactions.contains(t)) {
            transactions.add(t);
            totalDollarValue += t.getDollarAmount();
            totalCentValue += t.getCentAmount();
        }
    }

    public void removeTransaction(Transaction t) {
        if(transactions.contains(t)) transactions.remove(t);
    }

    public ArrayList<Transaction> getTransactions() { return transactions; }
    public void setTransactions(ArrayList<Transaction> transactions) { this.transactions = transactions;}

    public void updateTotalValue(){
        totalDollarValue = 0;
        totalCentValue = 0;
        for(Transaction transaction : transactions) {
            totalDollarValue += transaction.getDollarAmount();
            totalCentValue += transaction.getCentAmount();
        }
    }

    public double getTotalValue() {
        double dollars = (double) totalDollarValue;
        double cents = ((double) totalCentValue)/100.;
        return dollars+cents;
    }
    public void setTotalValue(int dollars, int cents) {
        this.totalDollarValue = dollars;
        this.totalCentValue = cents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
