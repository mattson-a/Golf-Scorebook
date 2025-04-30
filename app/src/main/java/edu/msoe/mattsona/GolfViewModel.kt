package edu.msoe.mattsona

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.msoe.mattsona.entities.Course
import edu.msoe.mattsona.entities.HoleStatistic
import edu.msoe.mattsona.entities.Round
import kotlinx.coroutines.launch
import java.util.Date

class GolfViewModel(application: Application): AndroidViewModel(application) {
    private val repository = GolfRepository.get()

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
}