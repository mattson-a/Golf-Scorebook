package edu.msoe.mattsona.database

import androidx.room.Dao
import androidx.room.Query
import edu.msoe.mattsona.entities.HoleStatistic
import java.util.UUID

@Dao
interface HoleStatisticDao {
    @Query("select * from holestatistic where roundId=(:roundId)")
    suspend fun getRoundStatistics(roundId : UUID) : List<HoleStatistic>
}