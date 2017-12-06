package com.jaz.budgt.database;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.jaz.budgt.database.entity.Statistic;

import java.util.ArrayList;

/**
 * Created by jaz on 12/4/17.
 */

public interface StatisticDAO {

    @Query("SELECT * FROM statistics")
    ArrayList<Statistic> getAll();

    @Query("SELECT * FROM statistics WHERE name LIKE :name LIMIT 1")
    Statistic findByName(String name);

    @Insert
    void insertAll(ArrayList<Statistic> statistics);

    @Update
    void update(Statistic statistic);

    @Delete
    void delete(Statistic statistic);
}
