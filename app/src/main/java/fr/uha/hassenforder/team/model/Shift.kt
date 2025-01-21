package fr.uha.hassenforder.team.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "shifts")
data class Shift(
    @PrimaryKey(autoGenerate = true)
    val sid : Long,
    val location : String,
    val date : String,
    val duration: Int,
    val driverId : Long,
)
