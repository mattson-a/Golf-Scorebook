package edu.msoe.mattsona.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Round (
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val courseId: Long,
    val date: Date,
    val holes: Int,
    //val score: Int,
    //add more here
)