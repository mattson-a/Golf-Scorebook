package edu.msoe.mattsona.database

import androidx.room.TypeConverter
import java.util.Date

class GolfDatabaseTypeConverters {
    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long): Date {
        return Date(millisSinceEpoch)
    }
}