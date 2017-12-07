package com.jaz.budgt.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by jaz on 11/26/17.
 */

public class DateTypeConverter {

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toLong(Date date) {
        return date == null ? null : date.getTime();
    }
}
