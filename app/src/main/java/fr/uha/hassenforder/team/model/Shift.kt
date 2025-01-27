package fr.uha.hassenforder.team.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shifts")
data class Shift(
    @PrimaryKey(autoGenerate = true)
    val sid : Long,
    val location : String,
    val startDate : String,
    val endDate: String,
    val vehicleId : Long,
    val driverId : Long,
)
