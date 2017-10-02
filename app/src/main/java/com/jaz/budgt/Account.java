package com.jaz.budgt;

import java.util.ArrayList;

/**
 * Created by jaz on 9/30/17.
 */

public class Account {

    //todo implement: each account should keep its own list of transactions associated with it

    ArrayList<Transaction> transactions = new ArrayList<>(0);
    private double totalValue = 0.0;
    private String name;

    public Account() { this.name = "Unnamed"; }

    public Account(String n) { this.name = n; }


    public void addTransaction(Transaction t) {
        if(!transactions.contains(t)) transactions.add(t);
    }

    public void removeTransaction(Transaction t) {
        if(transactions.contains(t)) transactions.remove(t);
    }

    public ArrayList<Transaction> getTransactions() { return transactions; }
    public void setTransactions(ArrayList<Transaction> transactions) { this.transactions = transactions;}

    public double getTotalValue() { return totalValue; }
    public void setTotalValue(double totalValue) { this.totalValue = totalValue; }
}
