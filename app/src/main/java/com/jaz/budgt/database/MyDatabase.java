package com.jaz.budgt.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import com.jaz.budgt.database.entity.Transaction;

/**
 * Created by jaz on 11/26/17.
 */

@Database(entities = {Transaction.class}, version = 1)
@TypeConverters(DateTypeConverter.class)
public abstract class MyDatabase extends RoomDatabase {

    public abstract TransactionDAO transactionDAO();

//    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE product "
//                    + " ADD COLUMN price INTEGER");
//
//            // enable flag to force update products
//            App.get().setForceUpdate(true);
//        }
//    };
}
