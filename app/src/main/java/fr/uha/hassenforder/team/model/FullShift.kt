package fr.uha.hassenforder.team.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class FullShift(
    @Embedded
    val shift: Shift,

    @Relation(parentColumn = "vehicleId", entityColumn = "vid")
    val vehicle : Vehicle?,

    @Relation(parentColumn = "driverId", entityColumn = "did")
    val driver : Driver?,

    @Relation(parentColumn = "sid", entityColumn = "did", associateBy = Junction(ShiftDriverAssociation::class))
    val passengers : List<Driver>,
)
