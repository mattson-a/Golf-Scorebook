package edu.msoe.mattsona

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.msoe.mattsona.entities.Course
import edu.msoe.mattsona.entities.HoleStatistic
import edu.msoe.mattsona.entities.Round
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GolfViewModel(application: Application): AndroidViewModel(application) {
    private val repository = GolfRepository.get()
    private val courseLock = Mutex()

    /**
     * Creates a new round and returns the Long for the newly created round
     */
    suspend fun createNewRound(courseId: Long, date: Date, holesPlayed: Int) : Long {
        val newRound = Round(courseId = courseId, date = date, holes = holesPlayed)
        return repository.insertRound(newRound)
    }

    /**
     * Function will test to see if given course name exists in the DB - useful for manual input version (v1.0)
     * Returns: Long of CourseID if course is found, null if not found
     */
    suspend fun courseExists(courseName: String) : Long? {
        val courses = repository.getAllCourses()

        if (courses.isNotEmpty()) {
            for (c in courses) {
                if (c.name == courseName) {
                    return c.id
                }
            }
        }
        return null
    }

    /**
     * Function creates a new course object in the db and returns the respective new ID of the course
     */
    suspend fun createNewCourse(courseName: String): Long {
        val newCourse = Course(name = courseName)
        return repository.insertCourse(newCourse)
    }

    suspend fun getAllCourses(): List<Course> {
        return repository.getAllCourses()
    }

    suspend fun getAllRounds(): List<Round> {
        return repository.getAllRounds()
    }

    suspend fun getRound(id: Long): Round {
        return repository.getRound(id)
    }

    suspend fun getCourse(id: Long): Course {
        return repository.getCourse(id)
    }

    fun insertHoleStatistics(statistics: List<HoleStatistic>) {
        viewModelScope.launch {
            repository.insertHoleStatistics(statistics)
        }
    }

    fun clearDb() {
        viewModelScope.launch {
            repository.clearDb()
        }
    }

    suspend fun getRoundStatistics(roundId: Long): List<HoleStatistic> {
        return repository.getRoundStatistics(roundId)
    }

    /**
     * This function prepares and imports sample data into the database, containing sample rounds, courses, and statistics
     */
    fun prepareSampleData(roundData: Array<String>) {
        for (round in roundData) {
            try {
                val data = round.split("|") //obtaining the data for a single row entry

                //clean data
                val dateStr = data[0].trim()
                val courseName = data[1].trim()
                val holes = data[2].trim().toInt()
                val stats = data[3].trim().split(",")

                //derive data
                val formatter = SimpleDateFormat("M/d/yyyy", Locale.getDefault())
                val date = formatter.parse(dateStr)

                //save new round to db
                viewModelScope.launch {
                    var courseId: Long?

                    courseLock.withLock {
                        courseId = courseExists(courseName)
                        if (courseId == null) {
                            courseId = createNewCourse(courseName)
                        }
                    }
                    val roundId = createNewRound(courseId!!, date!!, holes)

                    //build holeStatistic objects for the round
                    val holeStatistics: MutableList<HoleStatistic> = emptyList<HoleStatistic>().toMutableList()
                    var holeNum = 1

                    for (hole in stats) {
                        //hole is "par:score"
                        val scoring = hole.split(":")
                        val par = scoring[0].toInt()
                        val score = scoring[1].toInt()
                        holeStatistics.add(HoleStatistic(roundId=roundId, holeNumber=holeNum++, holePar=par, holeScore=score))
                    }

                    //save holeStatistics to db
                    insertHoleStatistics(holeStatistics)
                }
            } catch (e: ParseException) {
                Log.d("GolfViewModel", "Failed to parse the given date string")
                continue
            }
        }
    }

    suspend fun dbIsEmpty(): Boolean {
        return repository.isDbEmpty()
    }

    /**
     * This function calculates the total score of a round given the roundId.
     * Returns: an Int of the calculated total score or -1 if any hole score is left blank (null) and therefore a score cannot be calculated.
     */
    suspend fun calculateTotalScore(roundId: Long): Int {
        val holeStats = getRoundStatistics(roundId)

        holeStats.forEach { hole ->
            if (hole.holeScore == null) return -1
        }

        return holeStats.sumOf { it.holeScore!! }
    }
}