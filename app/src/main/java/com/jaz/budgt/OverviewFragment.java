package com.jaz.budgt;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by jaz on 8/22/17.
 */

public class OverviewFragment extends Fragment {
    public static OverviewFragment newInstance() {
        return new OverviewFragment();
    }
    //transaction list
    ArrayList<Transaction> transactionList = new ArrayList<>(0);
    LocalStorage localStorage;
    //variables for totals and stats
    double totalSpent = 0;
    double averagePerDay = 0.0;
    long totalDays = 0;
    Map<String, Double> categoryTotals = new Hashtable<>(60);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");//, Locale.US);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        localStorage = new LocalStorage(this.getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){inflater.inflate(R.menu.transaction_list_options, menu);}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.overview_fragment, container, false);

        transactionList = localStorage.loadTransactions();

        TextView totalSpentTextView = view.findViewById(R.id.total_amount_spent);
        totalSpentTextView.setText(calculateTotalSpent());

        TextView averagePerDayTextView = view.findViewById(R.id.average_per_day);
        averagePerDayTextView.setText(calculateAveragePerDay());

        TextView categoryTotalsTextView = view.findViewById(R.id.category_totals);
        calculateTotalsPerCategory();
        String categoryTotalString = "";
        Iterator it = categoryTotals.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            categoryTotalString += "\n" + pair.getKey() + ": $";
            NumberFormat formatter = new DecimalFormat("#0.00");
            categoryTotalString += formatter.format(pair.getValue());
        }
        categoryTotalsTextView.setText(categoryTotalString);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        localStorage.saveTransactions(transactionList);
    }

    @Override
    public void onStop() {
        super.onStop();
        localStorage.saveTransactions(transactionList);
    }

    @Override
    public void onResume() {
        super.onResume();
        transactionList = localStorage.loadTransactions();
        TextView totalSpent = super.getView().findViewById(R.id.total_amount_spent);
        totalSpent.setText(calculateTotalSpent());
    }
    
    public String calculateTotalSpent() {
        int dollarSum = 0;
        int centSum = 0;
        for (Transaction transaction: transactionList) {
            if(transaction.isExpense()) {
                dollarSum -= transaction.getDollarAmount();
                centSum -= transaction.getCentAmount();
            } else {
                dollarSum += transaction.getDollarAmount();
                centSum += transaction.getCentAmount();
            }
        }
        dollarSum += centSum / 100;
        centSum = centSum % 100;
        totalSpent = dollarSum + centSum / 100.;
        Log.d("Overview Fragment","total spent: " + totalSpent);
        String total = "";
        if(dollarSum < 0) {
            total += "-";
            dollarSum = Math.abs(dollarSum);
            centSum = Math.abs(centSum);
        }
        total += "$" + dollarSum + ".";
        if(centSum < 10) {
            total += "0" + centSum;
        } else {
            total += centSum;
        }
        total = "Total Spent: " + total;
        return total;
    }

    public String calculateAveragePerDay() {
        Date minDate = new Date();
        try { minDate = simpleDateFormat.parse("10/10/2100");
        } catch(ParseException e){ e.printStackTrace(); }
        Date maxDate = new Date();

        for (Transaction transaction: transactionList) {
            Date curDate = new Date();
            try {
                curDate = simpleDateFormat.parse(transaction.getDay() + "/" + transaction.getMonth() + "/" + transaction.getYear());
            } catch(ParseException e){
                e.printStackTrace();
            }
            if(curDate.getTime() > maxDate.getTime()) {
                maxDate = curDate;
            } else if(curDate.getTime() < minDate.getTime()) {
                minDate = curDate;
            }
        }
        //todo make this catch exceptions with initialization

        Log.d("OverviewFragment", "Mindate: " + minDate.toString());
        Log.d("OverviewFragment", "Maxdate: " + maxDate.toString());

        totalDays = 0;
        totalDays = maxDate.getTime() - minDate.getTime();
        //TimeUnit.DAYS.convert(totalDays, TimeUnit.MILLISECONDS);
        totalDays /= 86400000;
        Log.d("OverviewFragment", "Calculated diff between min/max dates: " + totalDays);

        if(totalDays == 0) {
            return "$0.00";
        }
        averagePerDay = totalSpent / (double) totalDays;
        Log.d("OverviewFragment", "Calculated average spent/day: " + averagePerDay);
        String average = Double.toString(Math.abs(averagePerDay));
        average = average.substring(0,average.indexOf('.')+2);
        if(averagePerDay < 0) average = "-$" + average;
        else average = "$" + average;
        return "Average Spent Per Day: " + average;
    }

    public void calculateTotalsPerCategory() {
        for(Transaction transaction : transactionList) {
            String category = transaction.getCategory();
            if(categoryTotals.containsKey(category)) {
                categoryTotals.put(category, categoryTotals.get(category) + transaction.getAmount());
            } else {
                categoryTotals.put(category,transaction.getAmount());
            }
        }

    }

}






