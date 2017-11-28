package com.jaz.budgt.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by jaz on 11/26/17.
 */

@Entity(tableName = "statistics")
public class Statistics {

    // Fields
    @PrimaryKey
    private String name;
    @ColumnInfo(name = "value")
    private double value;
    @ColumnInfo(name = "start_date")
    private Date startDate;
    @ColumnInfo(name = "end_date")
    private Date endDate;

    /* initial statistics:
    * average per day, week, month
    *
    * */

}
