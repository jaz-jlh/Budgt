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
    private static final String ACCOUNT_TAG = "Accounts";
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

    public ArrayList<Account> loadAccounts() {
        ArrayList<Account> accounts;
        sharedPreferences = this.activity.getSharedPreferences(this.activity.getString(R.string.accounts_file_name), Context.MODE_PRIVATE);
        String jsonAccountList = sharedPreferences.getString(ACCOUNT_TAG,"");
        Type type = new TypeToken<ArrayList<Account>>() {}.getType();
        accounts = gson.fromJson(jsonAccountList, type);
        if(accounts == null) { accounts = new ArrayList<>(0); }
        return accounts;
    }

    public void saveAccounts(ArrayList<Account> accounts) {
        sharedPreferences = this.activity.getSharedPreferences(this.activity.getString(R.string.accounts_file_name), Context.MODE_PRIVATE);
        prefsEditor = sharedPreferences.edit();
        String jsonAccountList = gson.toJson(accounts);
        prefsEditor.putString(ACCOUNT_TAG, jsonAccountList);
        prefsEditor.apply();
    }

    public void deleteAccounts() {
        ArrayList<Account> accounts = new ArrayList<>(0);
        sharedPreferences = this.activity.getSharedPreferences(this.activity.getString(R.string.accounts_file_name), Context.MODE_PRIVATE);
        prefsEditor = sharedPreferences.edit();
        String jsonAccountList = gson.toJson(accounts);
        prefsEditor.putString(ACCOUNT_TAG, jsonAccountList);
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
