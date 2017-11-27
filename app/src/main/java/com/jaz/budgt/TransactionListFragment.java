package com.jaz.budgt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.jaz.budgt.adapters.TransactionListAdapter;
import com.jaz.budgt.database.entity.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Created by jaz on 8/22/17.
 */

public class TransactionListFragment extends Fragment {
    public static TransactionListFragment newInstance() {
        return new TransactionListFragment();
    }
    //Intents
    static final int NEW_TRANSACTION_REQUEST = 1;
    static final int EDIT_TRANSACTION_REQUEST = 2;
    static final int RESULT_OK = 2;
    //List of transactions
    ArrayList<Transaction> transactionList = new ArrayList<>(0);
    String[] filterOptions = {"Today","Last Week","Last 2 Weeks","Last Month","Last 2 Months","Year to Date","All time"};
    ArrayList<Transaction> filteredTransactionList = new ArrayList<>(0); //todo finish implementing this
    ArrayList<Account> accounts = new ArrayList<>(0);
    private ListView listview;
    LocalStorage localStorage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localStorage  = new LocalStorage(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.transaction_list_fragment, container, false);
        transactionList = localStorage.loadTransactions();
        accounts = localStorage.loadAccounts();

        Log.d("TransactionListFragment","Number of transactions loaded: " + transactionList.size());
        Collections.sort(transactionList, Transaction.transactionDateComparator);
//        String temp = "";
//        for(Transaction transaction : transactionList) {
//            temp += transaction.getStringAmount() + "\n";
//        }
//        Log.d("TransactionListFragment",temp);
        final TransactionListAdapter transactionListAdapter = new TransactionListAdapter(getContext(), filteredTransactionList);
        listview = view.findViewById(R.id.transaction_list);
        listview.setAdapter(transactionListAdapter);

        Spinner transactionFilterSpinner = view.findViewById(R.id.transaction_filter_spinner);
        transactionFilterSpinner.setSelection(1);
        transactionFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("TransactionListFragment",filterOptions[i]);
                Date today = Calendar.getInstance().getTime();
                String todayString = today.toString();
                filteredTransactionList.clear();
                switch(i) {
                    case 0:
                        //Today
                        for(Transaction t : transactionList) {
                            if(t.calculateDayDifference(today) == 0) {
                                filteredTransactionList.add(t);
                            } else break;
                        }
                        break;
                    case 1:
                        //Last Week
                        for(Transaction t : transactionList) {
                            if(t.calculateDayDifference(today) <= 7) {
                                filteredTransactionList.add(t);
                            } else break;
                        }
                        break;
                    case 2:
                        //Last 2 Weeks
                        for(Transaction t : transactionList) {
                            if(t.calculateDayDifference(today) <= 14) {
                                filteredTransactionList.add(t);
                            } else break;
                        }
                        break;
                    case 3:
                        //Last Month
                        for(Transaction t : transactionList) {
                            if(t.calculateDayDifference(today) <= 30) {
                                filteredTransactionList.add(t);
                            } else break;
                        }
                        break;
                    case 4:
                        //Last 2 Months
                        for(Transaction t : transactionList) {
                            if(t.calculateDayDifference(today) <= 60) {
                                filteredTransactionList.add(t);
                            } else break;
                        }
                        break;
                    case 5:
                        //Year to Date
                        for(Transaction t : transactionList) {
                            if(t.getYear() == Integer.parseInt(todayString.substring(todayString.length()-4))) {
                                filteredTransactionList.add(t);
                            } else break;
                        }
                        break;
                    case 6:
                        //All time
                        //filteredTransactionList = transactionList;
                        break;
                }
                transactionListAdapter.notifyDataSetChanged();
                listview.setAdapter(transactionListAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Date today = Calendar.getInstance().getTime();
                filteredTransactionList.clear();
                for(Transaction t : transactionList) {
                    if (t.calculateDayDifference(today) < 7) {
                        filteredTransactionList.add(t);
                    }
                }
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                // allow the user to edit or delete the transaction
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Edit/Delete Transaction");
                builder.setMessage(transactionList.get(position).toString());
                // Set up the buttons
                builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO load the addtransaction activity but make it edit the transaction and have a save button
                        //todo this is much more complicated than you would think
                        Intent editTransactionIntent = new Intent(getContext(), AddTransactionActivity.class);
                        editTransactionIntent.putExtra(getString(R.string.edit_transaction),true);
                    }
                });
                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        transactionList.remove(position);
                        //todo make sure to remove the transaction from the account as well
                        transactionListAdapter.notifyDataSetChanged();
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
                Intent addTransactionIntent = new Intent(getContext(), AddTransactionActivity.class);
                addTransactionIntent.putExtra(getString(R.string.edit_transaction),false);
                startActivityForResult(addTransactionIntent,NEW_TRANSACTION_REQUEST);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TransactionListFragment","Got a result");
        if(requestCode > 0 && data != null) {
            //Transaction newTransaction = new Transaction(data.getStringExtra("NewTransaction"));
            //Log.d("TransactionListFragment","The transaction we received is: " + newTransaction.toString());
            //transactionList.add(newTransaction);
            Collections.sort(transactionList,Transaction.transactionDateComparator);
            //Log.d("TransactionListFragment","The list now looks like this: " + transactionList.toString());
            localStorage.saveTransactions(transactionList);
        } else {
            Log.d("TransactionListFragment","Looks like the new transaction was cancelled");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){inflater.inflate(R.menu.transaction_list_options, menu);}


    @Override
    public void onPause() {
        super.onPause();
        localStorage.saveTransactions(transactionList);
        //todo only save on modify
    }

    @Override
    public void onStop() {
        super.onStop();
        localStorage.saveTransactions(transactionList);
    }

    @Override
    public void onResume() {
        super.onResume();
        transactionList = localStorage.loadTransactions();
        Collections.sort(transactionList,Transaction.transactionDateComparator);
        listview = super.getView().findViewById(R.id.transaction_list);
        TransactionListAdapter adapter = new TransactionListAdapter(getContext(), transactionList);
        listview.setAdapter(adapter);
    }
}