package com.jaz.budgt;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.jaz.budgt.database.entity.Transaction;

import java.util.Date;
import java.util.List;

/**
 * Created by jaz on 12/7/17.
 */

public class TransactionListViewModel extends ViewModel {
    private MutableLiveData<List<Transaction>> transactions;

    public LiveData<List<Transaction>> getTransactions() {
        if(transactions == null) {
            transactions = new MutableLiveData<>();
            loadTransactions();
        }
        return transactions;
    }

    private void loadTransactions() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                transactions = App.get().getDatabase().transactionDAO().getAllSync();
            }
        }).start();
    }

    public LiveData<List<Transaction>> getTimePeriodTransactions(Date start, Date end) {
        if(transactions == null) {
            transactions = new MutableLiveData<>();
            loadTimePeriodTransactions(start, end);
        }
        return transactions;
    }

    private void loadTimePeriodTransactions(final Date start, final Date end) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                transactions = App.get().getDatabase().transactionDAO().getTransactionsForDateRangeSync(start,end);
            }
        }).start();
    }

}
