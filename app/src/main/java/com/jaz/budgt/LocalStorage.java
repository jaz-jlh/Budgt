package com.jaz.budgt;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jaz on 9/24/17.
 */

public class LocalStorage {
    private static final String TRANSACTIONS_TAG = "Transactions";
    private static final String CATEGORIES_TAG = "Categories";
    private static final String PAYMENT_TYPE_TAG = "Payment Type";
    private Gson gson = new Gson();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor prefsEditor;
    private Activity activity;

    //TODO maybe make this keep the lists as fields that get written and accessed by other classes

    public LocalStorage(Activity activity) {  this.activity = activity;  }

    public ArrayList<Transaction> loadTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>(0);
        sharedPreferences = this.activity.getSharedPreferences(this.activity.getString(R.string.transactions_file_name), Context.MODE_PRIVATE);
        String jsonTransactionList = sharedPreferences.getString(TRANSACTIONS_TAG,"");
        Type type = new TypeToken<ArrayList<Transaction>>() {}.getType();
        transactions = gson.fromJson(jsonTransactionList, type);
        if(transactions == null) { transactions = new ArrayList<>(0); }
        return transactions;
    }

    public void saveTransactions(ArrayList<Transaction> transactions) {
        sharedPreferences = this.activity.getSharedPreferences(this.activity.getString(R.string.transactions_file_name), Context.MODE_PRIVATE);
        prefsEditor = sharedPreferences.edit();
        String jsonTransactionList = gson.toJson(transactions);
        prefsEditor.putString(TRANSACTIONS_TAG, jsonTransactionList);
        prefsEditor.apply();
    }

    public void deleteTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>(0);
        sharedPreferences = this.activity.getSharedPreferences(this.activity.getString(R.string.transactions_file_name), Context.MODE_PRIVATE);
        prefsEditor = sharedPreferences.edit();
        String jsonTransactionList = gson.toJson(transactions);
        prefsEditor.putString(TRANSACTIONS_TAG, jsonTransactionList);
        prefsEditor.apply();
    }

    public ArrayList<String> loadPaymentTypes() {
        ArrayList<String> paymentTypes = new ArrayList<>(0);
        sharedPreferences = this.activity.getSharedPreferences(this.activity.getString(R.string.payment_types_file_name), Context.MODE_PRIVATE);
        String jsonPaymentTypeList = sharedPreferences.getString(PAYMENT_TYPE_TAG,"");
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        paymentTypes = gson.fromJson(jsonPaymentTypeList, type);
        if(paymentTypes == null) { paymentTypes = new ArrayList<>(0); }
        return paymentTypes;
    }

    public void savePaymentTypes(ArrayList<String> paymentTypes) {
        sharedPreferences = this.activity.getSharedPreferences(this.activity.getString(R.string.payment_types_file_name), Context.MODE_PRIVATE);
        prefsEditor = sharedPreferences.edit();
        String jsonPaymentTypeList = gson.toJson(paymentTypes);
        prefsEditor.putString(PAYMENT_TYPE_TAG, jsonPaymentTypeList);
        prefsEditor.apply();
    }

    public void deletePaymentTypes() {
        ArrayList<String> paymentTypes = new ArrayList<>(0);
        sharedPreferences = this.activity.getSharedPreferences(this.activity.getString(R.string.payment_types_file_name), Context.MODE_PRIVATE);
        prefsEditor = sharedPreferences.edit();
        String jsonPaymentTypeList = gson.toJson(paymentTypes);
        prefsEditor.putString(PAYMENT_TYPE_TAG, jsonPaymentTypeList);
        prefsEditor.apply();
    }

    public Map<String,ArrayList<String>> loadCategories() {
        Map<String,ArrayList<String>> categories;
        sharedPreferences = this.activity.getSharedPreferences(this.activity.getString(R.string.categories_file_name), Context.MODE_PRIVATE);
        String jsonCategories = sharedPreferences.getString(CATEGORIES_TAG,"");
        Type type = new TypeToken<Map<String,ArrayList<String>>>() {}.getType();
        categories = gson.fromJson(jsonCategories, type);
        if(categories == null) { categories = new HashMap<>(0); }
        return categories;
    }

    public void saveCategories(Map<String,ArrayList<String>> categories) {
        //todo maybe add check to see if size has changed and return true (maybe even send toast from caller)
        sharedPreferences = this.activity.getSharedPreferences(this.activity.getString(R.string.categories_file_name), Context.MODE_PRIVATE);
        prefsEditor = sharedPreferences.edit();
        String jsonCategories = gson.toJson(categories);
        prefsEditor.putString(CATEGORIES_TAG, jsonCategories);
        prefsEditor.apply();
    }

}
