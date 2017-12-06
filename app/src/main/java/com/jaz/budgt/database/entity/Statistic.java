package com.jaz.budgt.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by jaz on 11/26/17.
 */

@Entity(tableName = "statistics")
public class Statistic {

    // Fields
    @PrimaryKey @NonNull
    private String name = "";
    @ColumnInfo(name = "double_value")
    private double doubleValue;
    @ColumnInfo(name = "int_value")
    private int intValue;

    @NonNull
    public String getName() { return name; }
    public void setName(@NonNull String name) { this.name = name; }

    public double getDoubleValue() { return doubleValue; }
    public void setDoubleValue(double doubleValue) { this.doubleValue = doubleValue; }

    public int getIntValue() { return intValue; }
    public void setIntValue(int intValue) { this.intValue = intValue; }
}
