package edu.msoe.mattsona.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.msoe.mattsona.entities.Course
import java.util.UUID

@Dao
interface CourseDao {
    @Query("select * from course")
    suspend fun getAllCourses() : List<Course>

    @Query("select * from course where id=(:id)")
    suspend fun getCourse(id: UUID): Course

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: Course)

    @Query("select distinct * from course where name=(:courseName)")
    suspend fun getCourseByName(courseName: String): Course
}