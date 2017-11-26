package com.jaz.budgt;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by jaz on 11/25/17.
 */

public class AccountTransactionListActivity extends AppCompatActivity {
    LocalStorage localStorage;
    private ListView listview;
    ArrayList<Transaction> transactionList = new ArrayList<>(0);
    ArrayList<Account> accounts = new ArrayList<>(0);
    Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_transaction_list_activity);
        localStorage  = new LocalStorage(this);
        accounts = localStorage.loadAccounts();
        Bundle extras = getIntent().getExtras();
        int which = extras.getInt(getString(R.string.which_account));
        account = accounts.get(which);
        transactionList = account.getTransactions();
        this.setTitle("Transactions for " + account.getName());
        final TransactionListAdapter transactionListAdapter = new TransactionListAdapter(getApplicationContext(), transactionList);
        listview = (ListView)findViewById(R.id.account_transaction_list);
        listview.setAdapter(transactionListAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                // allow the user to edit or delete the transaction
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountTransactionListActivity.this);
                builder.setTitle("Edit/Delete Transaction");
                builder.setMessage("DELETING HERE IS STILL SEPARATE FROM DELETING FROM TRANSACTION LIST");
                // Set up the buttons
                builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO load the addtransaction activity but make it edit the transaction and have a save button
                        //todo this is much more complicated than you would think
                        Intent editTransactionIntent = new Intent(getApplicationContext(), AddTransactionActivity.class);
                        editTransactionIntent.putExtra(getString(R.string.edit_transaction),true);
                    }
                });
                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        transactionList.remove(position);
                        //todo make sure to remove the transaction from the transaction list as well
                        transactionListAdapter.notifyDataSetChanged();
                        //todo make this actually refresh view
                    }
                });
                builder.show();
            }
        });
    }
}
