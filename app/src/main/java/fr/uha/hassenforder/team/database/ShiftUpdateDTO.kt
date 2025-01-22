package fr.uha.hassenforder.team.database

import androidx.room.ColumnInfo
import androidx.room.Ignore
import java.util.Date

sealed class ShiftUpdateDTO {

    data class Location(
        val sid: Long,
        val location: String
    ) : ShiftUpdateDTO()

    data class Date(
        val sid: Long,
        val date: String
    ) : ShiftUpdateDTO()

    data class Duration(
        val sid: Long,
        val duration: Int
    ) : ShiftUpdateDTO()

    data class Vehicle(
        val sid: Long,
        val vehicleId: Long
    ) : ShiftUpdateDTO()

    data class Driver(
        val sid: Long,
        val driverId: Long
    ) : ShiftUpdateDTO()
}
