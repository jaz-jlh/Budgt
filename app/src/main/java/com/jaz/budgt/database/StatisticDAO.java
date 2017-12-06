package com.jaz.budgt.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.jaz.budgt.database.entity.Statistic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaz on 12/4/17.
 */
@Dao
public interface StatisticDAO {

    @Query("SELECT * FROM statistics")
    List<Statistic> getAll();

    @Query("SELECT * FROM statistics WHERE name LIKE :name LIMIT 1")
    Statistic findByName(String name);

    @Insert
    void insertAll(List<Statistic> statistics);

    @Update
    void update(Statistic statistic);

    @Delete
    void delete(Statistic statistic);
}
