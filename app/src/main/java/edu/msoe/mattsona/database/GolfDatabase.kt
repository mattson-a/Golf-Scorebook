package edu.msoe.mattsona.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.msoe.mattsona.entities.Course
import edu.msoe.mattsona.entities.HoleStatistic
import edu.msoe.mattsona.entities.Round

@Database(entities=[Round::class, Course::class, HoleStatistic::class], version=3, exportSchema=false)
@TypeConverters(GolfDatabaseTypeConverters::class)
abstract class GolfDatabase : RoomDatabase() {
    abstract fun roundDao() : RoundDao
    abstract fun courseDao() : CourseDao
    abstract fun statisticsDao() : HoleStatisticDao
}