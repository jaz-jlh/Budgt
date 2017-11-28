package com.jaz.budgt.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.jaz.budgt.database.entity.Transaction;

import java.util.ArrayList;

/**
 * Created by jaz on 11/26/17.
 */

@Dao
public interface TransactionDAO {
    @Query("SELECT * FROM transactions")
    ArrayList<Transaction> getAll();

//    @Query("SELECT * FROM transactions WHERE name LIKE :name LIMIT 1")
//    Transaction findByName(String name);

    @Query("SELECT * FROM transactions WHERE account LIKE :account")
    ArrayList<Transaction> getAccountTransactionList(String account);

    @Query("SELECT * FROM transactions WHERE date >= startDate AND date <= endDate")
    ArrayList<Transaction> getTransactionsForDateRange(Long startDate, Long endDate);

    @Insert
    void insertAll(ArrayList<Transaction> transactions);

    @Update
    void update(Transaction transaction);

    @Delete
    void delete(Transaction transaction);
}
