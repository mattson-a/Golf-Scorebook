package edu.msoe.mattsona.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class HoleStatistic (
    @PrimaryKey val id: UUID,
    val roundId: UUID,
    val holePar: Int,
    val holeScore: Int,
    //add more here and potentially toggleable ones with nullable values (val fir: String?)
)