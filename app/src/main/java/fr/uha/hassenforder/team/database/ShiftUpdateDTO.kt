package fr.uha.hassenforder.team.database

sealed class ShiftUpdateDTO {

    data class Location(
        val sid: Long,
        val location: String
    ) : ShiftUpdateDTO()

    data class StartDate(
        val sid: Long,
        val startDate: String
    ) : ShiftUpdateDTO()

    data class EndDate(
        val sid: Long,
        val endDate: String
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
