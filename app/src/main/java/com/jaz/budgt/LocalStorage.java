package com.jaz.budgt;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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

    //todo make the methods static and include pass the activity
    //need to put gson instantiation inside each method

    public LocalStorage(Activity activity) {  this.activity = activity;  }

    public ArrayList<Transaction> loadTransactions() {
        Log.d("LocalStorage","Loading transactions...");
        ArrayList<Transaction> transactions;
        sharedPreferences = this.activity.getSharedPreferences(this.activity.getString(R.string.transactions_file_name), Context.MODE_PRIVATE);
        String jsonTransactionList = sharedPreferences.getString(TRANSACTIONS_TAG,"");
        Type type = new TypeToken<ArrayList<Transaction>>() {}.getType();
        try {
            transactions = gson.fromJson(jsonTransactionList, type);
        } catch (com.google.gson.JsonSyntaxException e) {
            transactions = null;
            e.printStackTrace();
        }
        if(transactions == null) { transactions = new ArrayList<>(0); }
        return transactions;
    }

    public void saveTransactions(ArrayList<Transaction> transactions) {
        Log.d("LocalStorage","Saving transactions...");
        sharedPreferences = this.activity.getSharedPreferences(this.activity.getString(R.string.transactions_file_name), Context.MODE_PRIVATE);
        prefsEditor = sharedPreferences.edit();
        String jsonTransactionList = gson.toJson(transactions);
        prefsEditor.putString(TRANSACTIONS_TAG, jsonTransactionList);
        prefsEditor.apply();
    }

    public void deleteTransactions() {
        Log.d("LocalStorage","Deleting transactions...");
        ArrayList<Transaction> transactions = new ArrayList<>(0);
        sharedPreferences = this.activity.getSharedPreferences(this.activity.getString(R.string.transactions_file_name), Context.MODE_PRIVATE);
        prefsEditor = sharedPreferences.edit();
        String jsonTransactionList = gson.toJson(transactions);
        prefsEditor.putString(TRANSACTIONS_TAG, jsonTransactionList);
        prefsEditor.apply();
    }

    public ArrayList<Account> loadAccounts() {
        Log.d("LocalStorage","Loading Accounts...");
        ArrayList<Account> accounts;
        sharedPreferences = this.activity.getSharedPreferences(this.activity.getString(R.string.accounts_file_name), Context.MODE_PRIVATE);
        String jsonAccountList = sharedPreferences.getString(ACCOUNT_TAG,"");
        Type type = new TypeToken<ArrayList<Account>>() {}.getType();
        accounts = gson.fromJson(jsonAccountList, type);
        if(accounts == null) { accounts = new ArrayList<>(0); }
        return accounts;
    }

    public void saveAccounts(ArrayList<Account> accounts) {
        Log.d("LocalStorage","Saving accounts...");
        sharedPreferences = this.activity.getSharedPreferences(this.activity.getString(R.string.accounts_file_name), Context.MODE_PRIVATE);
        prefsEditor = sharedPreferences.edit();
        String jsonAccountList = gson.toJson(accounts);
        prefsEditor.putString(ACCOUNT_TAG, jsonAccountList);
        prefsEditor.apply();
    }

    public void deleteAccounts() {
        Log.d("LocalStorage","Deleting accounts...");
        ArrayList<Account> accounts = new ArrayList<>(0);
        sharedPreferences = this.activity.getSharedPreferences(this.activity.getString(R.string.accounts_file_name), Context.MODE_PRIVATE);
        prefsEditor = sharedPreferences.edit();
        String jsonAccountList = gson.toJson(accounts);
        prefsEditor.putString(ACCOUNT_TAG, jsonAccountList);
        prefsEditor.apply();
    }

    public Map<String,ArrayList<String>> loadCategories() {
        Log.d("LocalStorage","Loading categories...");
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
        Log.d("LocalStorage","Saving categories...");
        sharedPreferences = this.activity.getSharedPreferences(this.activity.getString(R.string.categories_file_name), Context.MODE_PRIVATE);
        prefsEditor = sharedPreferences.edit();
        String jsonCategories = gson.toJson(categories);
        prefsEditor.putString(CATEGORIES_TAG, jsonCategories);
        prefsEditor.apply();
    }

}
