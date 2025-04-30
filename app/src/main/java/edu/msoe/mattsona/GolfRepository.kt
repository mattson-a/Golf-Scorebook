package edu.msoe.mattsona

import android.content.Context
import androidx.room.Room
import edu.msoe.mattsona.database.GolfDatabase
import edu.msoe.mattsona.entities.Course
import edu.msoe.mattsona.entities.HoleStatistic
import edu.msoe.mattsona.entities.Round

private const val DATABASE_NAME = "golf-scoring-database"

class GolfRepository private constructor(context: Context) {
    private val database: GolfDatabase = Room.databaseBuilder(
        context.applicationContext,
        GolfDatabase::class.java,
        DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    companion object {
        private var INSTANCE: GolfRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = GolfRepository(context)
            }
        }

        fun get(): GolfRepository {
            return INSTANCE ?:
            throw IllegalStateException("GolfRepository must be initialized")
        }
    }

    /** Start of Repository DB Functions **/

    suspend fun getAllRounds() : List<Round> {
        return database.roundDao().getAllRounds()
    }

    suspend fun getAllCourses() : List<Course> {
        return database.courseDao().getAllCourses()
    }

    suspend fun insertRound(round: Round): Long {
        return database.roundDao().insertRound(round)
    }

    suspend fun insertCourse(course: Course): Long {
        return database.courseDao().insertCourse(course)
    }

    suspend fun getCourse(courseId: Long): Course {
        return database.courseDao().getCourse(courseId)
    }

    suspend fun getRound(roundId: Long): Round {
        return database.roundDao().getRound(roundId)
    }

    suspend fun insertHoleStatistics(stats: List<HoleStatistic>) {
        return database.statisticsDao().insertHoleStatistics(stats)
    }

    suspend fun clearDb() {
        database.courseDao().deleteAllCourses()
        database.statisticsDao().deleteAllStatistics()
        database.roundDao().deleteAllRounds()
    }

    suspend fun getRoundStatistics(roundId: Long): List<HoleStatistic> {
        return database.statisticsDao().getRoundStatistics(roundId)
    }
}