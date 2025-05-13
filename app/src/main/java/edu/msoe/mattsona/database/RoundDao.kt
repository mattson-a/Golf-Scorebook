package edu.msoe.mattsona.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.msoe.mattsona.entities.Round

@Dao
interface RoundDao {
    @Query("select * from round")
    suspend fun getAllRounds(): List<Round>

    @Query("select * from round where id=(:id)")
    suspend fun getRound(id: Long): Round

    @Query("select * from round where courseId=(:courseId)")
    suspend fun getAllRoundsFromCourse(courseId: Long): List<Round>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRound(round: Round): Long

    @Query("delete from round")
    suspend fun deleteAllRounds()

    @Query("select count(*) from round")
    suspend fun count(): Int
}