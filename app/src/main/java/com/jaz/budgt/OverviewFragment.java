package com.jaz.budgt;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by jaz on 8/22/17.
 */

public class OverviewFragment extends Fragment {
    public static OverviewFragment newInstance() {
        return new OverviewFragment();
    }

    static final String TRANSACTIONS_TAG = "Transactions";
    ArrayList<Transaction> transactionList = new ArrayList<>(0);
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefsEditor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.overview_fragment, container, false);

        loadTransactions();

        TextView totalSpent = view.findViewById(R.id.total_amount_spent);
        totalSpent.setText(calculateTotalSpent());

        return view;
    }

    public void saveTransactions() {
        Gson gson = new Gson();
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.transactions_file_name), Context.MODE_PRIVATE);
        prefsEditor = sharedPreferences.edit();
        String jsonTransactionList = gson.toJson(transactionList);
        prefsEditor.putString(TRANSACTIONS_TAG, jsonTransactionList);
        prefsEditor.apply();
    }

    public void loadTransactions() {
        Gson gson = new Gson();
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.transactions_file_name), Context.MODE_PRIVATE);
        String jsonTransactionList = sharedPreferences.getString(TRANSACTIONS_TAG,"");
        Type type = new TypeToken<ArrayList<Transaction>>() {}.getType();
        transactionList = gson.fromJson(jsonTransactionList, type);
        if(transactionList == null) {
            transactionList = new ArrayList<>(0);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveTransactions();
    }

    @Override
    public void onStop() {
        super.onStop();
        saveTransactions();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTransactions();
        TextView totalSpent = super.getView().findViewById(R.id.total_amount_spent);
        totalSpent.setText(calculateTotalSpent());
    }
    
    public String calculateTotalSpent() {
        int dollarSum = 0;
        int centSum = 0;
        for (Transaction transaction: transactionList) {
            if(transaction.isExpense() == 1) {
                dollarSum -= transaction.getDollarAmount();
                centSum -= transaction.getCentAmount();
            } else {
                dollarSum += transaction.getDollarAmount();
                centSum += transaction.getCentAmount();
            }
        }
        dollarSum += centSum / 100;
        centSum = centSum % 100;
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
        return total;
    }
}
