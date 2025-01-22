package fr.uha.hassenforder.team.model

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "svas",
    primaryKeys = ["sid", "vid"],
    indices = [Index("sid"), Index("vid")]
)

data class ShiftVehiculeAssociation(
    val sid : Long,
    val vid : Long,
)