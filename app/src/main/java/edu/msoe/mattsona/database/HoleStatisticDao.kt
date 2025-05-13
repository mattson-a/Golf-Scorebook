package edu.msoe.mattsona.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.msoe.mattsona.entities.HoleStatistic

@Dao
interface HoleStatisticDao {
    @Query("select * from holestatistic where roundId=(:roundId)")
    suspend fun getRoundStatistics(roundId : Long) : List<HoleStatistic>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHoleStatistics(stats: List<HoleStatistic>)

    @Query("delete from holestatistic")
    suspend fun deleteAllStatistics()

    @Query("select count(*) from holestatistic")
    suspend fun count(): Int
}