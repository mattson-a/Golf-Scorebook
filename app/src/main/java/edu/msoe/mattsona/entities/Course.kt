package edu.msoe.mattsona.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Course (
    @PrimaryKey val id: UUID,
    val name: String,
    //val city: String,
    //val state: String, //state listed as two-letter code (WI, IL, etc.)
    //val par: Int,
    //add more here
)