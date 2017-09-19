package com.jaz.budgt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaz on 8/22/17.
 */

public class TransactionListFragment extends Fragment {
    public static TransactionListFragment newInstance() {
        return new TransactionListFragment();
    }
    static final int NEW_TRANSACTION_REQUEST = 1;
    static final int RESULT_OK = 2;
    static final String TRANSACTIONS_TAG = "Transactions";
    ArrayList<Transaction> transactionList = new ArrayList<>(0);
    private ListView listview;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefsEditor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_list_fragment, container, false);
        loadTransactions();

        listview = view.findViewById(R.id.transaction_list);
//        String[] transactionArray = new String[transactionList.size()];
//        for(int i=0; i <transactionList.size(); i++) {
//            Transaction t = transactionList.get(i);
//            transactionArray[i] = t.toString();
//        }

        TransactionListAdapter adapter = new TransactionListAdapter(getContext(), transactionList);
        listview.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddTransactionActivity.class);
                startActivityForResult(intent,NEW_TRANSACTION_REQUEST);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TransactionListFragment","Got a result");
        if(requestCode > 0) {
            Transaction newTransaction = new Transaction(data.getStringArrayExtra("NewTransaction"));
            Log.d("TransactionListFragment","The transaction we received is: " + newTransaction.toString());
            transactionList.add(newTransaction);
            Log.d("TransactionListFragment","The list now looks like this: " + transactionList.toString());
            saveTransactions();
        }
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
        listview = super.getView().findViewById(R.id.transaction_list);
//        String[] transactionArray = new String[transactionList.size()];
//        for(int i=0; i <transactionList.size(); i++) {
//            Transaction t = transactionList.get(i);
//            transactionArray[i] = t.toString();
//        }

        TransactionListAdapter adapter = new TransactionListAdapter(getContext(), transactionList);
        listview.setAdapter(adapter);
    }
}