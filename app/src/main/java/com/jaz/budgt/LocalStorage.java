package com.jaz.budgt;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by jaz on 9/24/17.
 */

public class LocalStorage extends AppCompatActivity {
    private static final String TRANSACTIONS_TAG = "Transactions";
    private static final String CATEGORIES_TAG = "Categories";
    private static final String PAYMENT_TYPE_TAG = "Payment Type";
    private Gson gson = new Gson();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor prefsEditor;

    public LocalStorage() {    }

    public ArrayList<Transaction> loadTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>(0);
        sharedPreferences = this.getSharedPreferences(getString(R.string.transactions_file_name), Context.MODE_PRIVATE);
        String jsonTransactionList = sharedPreferences.getString(TRANSACTIONS_TAG,"");
        Type type = new TypeToken<ArrayList<Transaction>>() {}.getType();
        transactions = gson.fromJson(jsonTransactionList, type);
        if(transactions == null) { transactions = new ArrayList<>(0); }
        return transactions;
    }

    public void saveTransactions(ArrayList<Transaction> transactions) {
        sharedPreferences = this.getSharedPreferences(getString(R.string.transactions_file_name), Context.MODE_PRIVATE);
        prefsEditor = sharedPreferences.edit();
        String jsonTransactionList = gson.toJson(transactions);
        prefsEditor.putString(TRANSACTIONS_TAG, jsonTransactionList);
        prefsEditor.apply();
    }

    public void deleteTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>(0);
        sharedPreferences = this.getSharedPreferences(getString(R.string.transactions_file_name), Context.MODE_PRIVATE);
        prefsEditor = sharedPreferences.edit();
        String jsonTransactionList = gson.toJson(transactions);
        prefsEditor.putString(TRANSACTIONS_TAG, jsonTransactionList);
        prefsEditor.apply();
    }

    public ArrayList<String> loadPaymentTypes() {
        ArrayList<String> paymentTypes = new ArrayList<>(0);
        sharedPreferences = this.getSharedPreferences(getString(R.string.payment_types_file_name), Context.MODE_PRIVATE);
        String jsonPaymentTypeList = sharedPreferences.getString(PAYMENT_TYPE_TAG,"");
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        paymentTypes = gson.fromJson(jsonPaymentTypeList, type);
        if(paymentTypes == null) { paymentTypes = new ArrayList<>(0); }
        return paymentTypes;
    }

    public void deletePaymentTypes() {
        ArrayList<String> paymentTypes = new ArrayList<>(0);
        sharedPreferences = this.getSharedPreferences(getString(R.string.payment_types_file_name), Context.MODE_PRIVATE);
        prefsEditor = sharedPreferences.edit();
        String jsonPaymentTypeList = gson.toJson(paymentTypes);
        prefsEditor.putString(PAYMENT_TYPE_TAG, jsonPaymentTypeList);
        prefsEditor.apply();
    }

    public ArrayList<String> loadCategories() {
        ArrayList<String> categories = new ArrayList<>(0);
        sharedPreferences = this.getSharedPreferences(getString(R.string.categories_file_name), Context.MODE_PRIVATE);
        String jsonCategoryList = sharedPreferences.getString(CATEGORIES_TAG,"");
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        categories = gson.fromJson(jsonCategoryList, type);
        if(categories == null) { categories = new ArrayList<>(0); }
        return categories;
    }


}
