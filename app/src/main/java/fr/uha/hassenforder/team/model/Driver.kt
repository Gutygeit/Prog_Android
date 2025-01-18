package fr.uha.hassenforder.team.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drivers")
data class Driver(
    @PrimaryKey(autoGenerate = true)
    val did : Long,
    val firstname : String,
    val lastname : String,
    val phone : String,
    val license: License
)
