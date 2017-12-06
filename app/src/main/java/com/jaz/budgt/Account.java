package com.jaz.budgt;

import com.jaz.budgt.database.entity.Transaction;

import java.util.ArrayList;

/**
 * Created by jaz on 9/30/17.
 */

public class Account {

    private int totalDollarValue = 0;
    private int totalCentValue = 0;
    private String name;

    public Account() { this.name = "Unnamed"; }

    public Account(String n) { this.name = n; }

   public void updateTotalValue(){
        totalDollarValue = 0;
        totalCentValue = 0;
        // todo use database here
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
