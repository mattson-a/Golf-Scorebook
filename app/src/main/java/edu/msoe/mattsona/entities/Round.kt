package edu.msoe.mattsona.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity
data class Round (
    @PrimaryKey val id: UUID,
    val courseId: UUID,
    val date: Date,
    val holes: Int,
    //val score: Int,
    //add more here
)