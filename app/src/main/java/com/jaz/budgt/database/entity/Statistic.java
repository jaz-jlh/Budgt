package com.jaz.budgt.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by jaz on 11/26/17.
 */

@Entity(tableName = "statistics")
public class Statistic {

    // Fields
    @PrimaryKey
    private String name;
    @ColumnInfo(name = "double_value")
    private double doubleValue;
    @ColumnInfo(name = "int_value")
    private int intValue;


    /* initial statistics:
    * average per day, week, month
    *
    * */

}
