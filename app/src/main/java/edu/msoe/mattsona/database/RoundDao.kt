package edu.msoe.mattsona.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.msoe.mattsona.entities.Round
import java.util.UUID

@Dao
interface RoundDao {
    @Query("select * from round")
    suspend fun getAllRounds(): List<Round>

    @Query("select * from round where id=(:id)")
    suspend fun getRound(id: UUID): Round

    @Query("select * from round where courseId=(:courseId)")
    suspend fun getAllRoundsFromCourse(courseId: UUID): List<Round>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRound(round: Round)
}