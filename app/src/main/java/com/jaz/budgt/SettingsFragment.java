package com.jaz.budgt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import static com.jaz.budgt.TransactionListFragment.TRANSACTIONS_TAG;

/**
 * Created by jaz on 8/22/17.
 */

public class SettingsFragment extends Fragment {
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefsEditor;

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);

        Button clearTransactionsButton = view.findViewById(R.id.clear_transactions_button);
        clearTransactionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTransactions();
                Toast.makeText(getContext(),getString(R.string.cleared_transactions),Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void deleteTransactions() {
        Gson gson = new Gson();
        ArrayList<Transaction> transactionList = new ArrayList<>(0);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.transactions_file_name), Context.MODE_PRIVATE);
        prefsEditor = sharedPreferences.edit();
        String jsonTransactionList = gson.toJson(transactionList);
        prefsEditor.putString(TRANSACTIONS_TAG, jsonTransactionList);
        prefsEditor.apply();
    }
}