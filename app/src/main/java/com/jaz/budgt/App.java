package com.jaz.budgt;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.jaz.budgt.database.MyDatabase;

/**
 * Created by jaz on 12/4/17.
 */

public class App extends Application {

    public static App INSTANCE;
    private static final String DATABASE_NAME = "MyDatabase";

    private MyDatabase database;

    public static App get() {
        return INSTANCE;
    }

    @Override public void onCreate() {
        super.onCreate();

        database = Room.databaseBuilder(getApplicationContext(),MyDatabase.class,DATABASE_NAME)
                .build();

        INSTANCE = this;
    }

    public MyDatabase getDatabase() {
        return database;
    }

}
