package com.jaz.budgt.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.jaz.budgt.database.entity.Statistic;
import com.jaz.budgt.database.entity.Transaction;

/**
 * Created by jaz on 11/26/17.
 */

@Database(entities = {Transaction.class, Statistic.class}, version = 1)
@TypeConverters({DateTypeConverter.class})
public abstract class MyDatabase extends RoomDatabase {

    public abstract TransactionDAO transactionDAO();
    public abstract StatisticDAO statisticsDAO();

}
