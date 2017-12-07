package com.jaz.budgt.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.jaz.budgt.database.entity.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jaz on 11/26/17.
 */

@Dao
public interface TransactionDAO {
    @Query("SELECT * FROM transactions")
    List<Transaction> getAll();

//    @Query("SELECT * FROM transactions WHERE name LIKE :name LIMIT 1")
//    Transaction findByName(String name);

    @Query("SELECT * FROM transactions WHERE account LIKE :account")
    List<Transaction> getAccountTransactionList(String account);

    @Query("SELECT * FROM transactions WHERE category LIKE :category")
    List<Transaction> getCategoryTransactionList(String category);

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate")
    List<Transaction> getTransactionsForDateRange(Date startDate, Date endDate);

    // Sync Versions
    @Query("SELECT * FROM transactions WHERE account LIKE :account")
    LiveData<List<Transaction>> getAccountTransactionListSync(String account);

    @Query("SELECT * FROM transactions WHERE category LIKE :category")
    LiveData<List<Transaction>> getCategoryTransactionListSync(String category);

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate")
    LiveData<List<Transaction>> getTransactionsForDateRangeSync(Date startDate, Date endDate);


    @Insert
    void insertAll(List<Transaction> transactions);

    @Update
    void update(Transaction transaction);

    @Delete
    void delete(Transaction transaction);
}
