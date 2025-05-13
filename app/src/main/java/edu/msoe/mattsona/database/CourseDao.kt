package edu.msoe.mattsona.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.msoe.mattsona.entities.Course

@Dao
interface CourseDao {
    @Query("select * from course")
    suspend fun getAllCourses() : List<Course>

    @Query("select * from course where id=(:id)")
    suspend fun getCourse(id: Long): Course

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: Course): Long

    @Query("delete from course")
    suspend fun deleteAllCourses()

    @Query("select count(*) from course")
    suspend fun count(): Int
}