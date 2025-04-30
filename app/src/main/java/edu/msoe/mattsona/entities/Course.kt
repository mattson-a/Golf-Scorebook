package edu.msoe.mattsona.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Course (
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    //val city: String,
    //val state: String, //state listed as two-letter code (WI, IL, etc.)
    //val par: Int,
    //add more here
)