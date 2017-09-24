package com.jaz.budgt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

    // TODO condense into single class; currently accessed by other classes
    static final String TRANSACTIONS_TAG = "Transactions";
    static final String CATEGORIES_TAG = "Categories";
    static final String PAYMENT_TYPE_TAG = "Payment Type";

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

        final TransactionListAdapter adapter = new TransactionListAdapter(getContext(), transactionList);
        listview.setAdapter(adapter);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                // allow the user to edit or delete the transaction
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Edit/Delete Transaction");
                // Set up the buttons
                builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO load the addtransaction activity but make it edit the transaction
                    }
                });
                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        transactionList.remove(position);
                        adapter.notifyDataSetChanged();
                        //todo make this actually refresh view
                    }
                });
                builder.show();
            }
        });

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
        if(requestCode > 0 && data != null) {
            Transaction newTransaction = new Transaction(data.getStringArrayExtra("NewTransaction"));
            Log.d("TransactionListFragment","The transaction we received is: " + newTransaction.toString());
            transactionList.add(newTransaction);
            Log.d("TransactionListFragment","The list now looks like this: " + transactionList.toString());
            saveTransactions();
        } else {
            Log.d("TransactionListFragment","Looks like the new transaction was cancelled");
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

    public void loadTransactions() { //TODO replace all of these methods with a storage manager
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
        TransactionListAdapter adapter = new TransactionListAdapter(getContext(), transactionList);
        listview.setAdapter(adapter);
    }
}