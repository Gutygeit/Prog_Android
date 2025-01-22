package fr.uha.hassenforder.team.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class Vehicle(
    @PrimaryKey(autoGenerate = true)
    val vid : Long,
    val brandAndModel : String,
    val matriculation : String,
    val kilometers : Int,
    val status : VehicleStatus
)
