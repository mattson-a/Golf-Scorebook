package edu.msoe.mattsona

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.msoe.mattsona.entities.Course
import edu.msoe.mattsona.entities.Round
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class GolfViewModel(application: Application): AndroidViewModel(application) {
    private var uuidSeed: Int = 0
    private val repository = GolfRepository.get()

    private fun getNextUUID(): UUID {
        return uuidFromInt(uuidSeed++)
    }

    private fun uuidFromInt(value: Int, useAsMostSignificantBits: Boolean = false): UUID {
        val longValue = value.toLong()
        val mostSignificantBits = if (useAsMostSignificantBits) longValue else 0L
        val leastSignificantBits = if (!useAsMostSignificantBits) longValue else 0L
        return UUID(mostSignificantBits, leastSignificantBits)
    }

    /**
     * Creates a new round and returns the UUID for the newly created round
     */
    fun createNewRound(courseId: UUID, date: Date, holesPlayed: Int) : UUID {
        val newRound = Round(getNextUUID(), courseId, date, holesPlayed)
        viewModelScope.launch {
            repository.insertRound(newRound)
        }

        return newRound.id
    }

    fun courseExists(courseName: String) : Boolean {
        var courses: List<Course> = emptyList()
        viewModelScope.launch {
            courses = repository.getAllCourses()
        }

        if (courses.isNotEmpty()) {
            for (c in courses) {
                if (c.name == courseName) {
                    return true
                }
            }
        }
        return false
    }

    fun createNewCourse(courseName: String) {
        val newCourse = Course(getNextUUID(), courseName)
        viewModelScope.launch {
            repository.insertCourse(newCourse)
        }
    }

    suspend fun getAllCourses(): List<Course> {
        return repository.getAllCourses()
    }

    suspend fun getAllRounds(): List<Round> {
        return repository.getAllRounds()
    }

//    init {
//        //load test data
//        viewModelScope.launch {
//            repository.clearDb()
//            repository.loadData()
//        }
//    }
}