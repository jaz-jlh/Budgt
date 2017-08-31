package com.jaz.budgt;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by jaz on 8/22/17.
 */

public class TransactionListFragment extends Fragment {
    public static TransactionListFragment newInstance() {
        return new TransactionListFragment();
    }
    static final int NEW_TRANSACTION_REQUEST = 1;
    static final int RESULT_OK = 2;
    ArrayList<Transaction> transactionList = new ArrayList<>(0);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_list_fragment, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
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
        if(requestCode == RESULT_OK) {
            Transaction newTransaction = new Transaction(data.getStringArrayExtra("NewTransaction"));
            transactionList.add(newTransaction);
        }
    }
}