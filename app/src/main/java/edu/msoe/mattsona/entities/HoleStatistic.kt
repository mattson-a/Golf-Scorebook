package edu.msoe.mattsona.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HoleStatistic (
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val roundId: Long,
    val holeNumber: Int,
    var holePar: Int?,
    var holeScore: Int?,
    //add more here and potentially toggleable ones with nullable values (val fir: String?)
)