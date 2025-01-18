package fr.uha.hassenforder.team.model

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "sdas",
    primaryKeys = ["sid", "did"],
    indices = [Index("sid"), Index("did")]
)
data class ShiftDriverAssociation(
    val sid : Long,
    val did : Long,
)
