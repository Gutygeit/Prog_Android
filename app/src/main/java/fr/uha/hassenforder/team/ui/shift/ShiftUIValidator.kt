package fr.uha.hassenforder.team.ui.shift

import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Driver
import fr.uha.hassenforder.team.model.FullShift
import fr.uha.hassenforder.team.model.Vehicle

@Suppress("UNUSED_PARAMETER")
class ShiftUIValidator (private val shift : FullShift) {

    fun validateLocation(newValue: String) : Int? {
        return when {
            newValue.isEmpty()  ->  R.string.value_empty
            newValue.isBlank()  ->  R.string.value_blank
            newValue.length < 3 ->  R.string.value_too_short
            else -> null
        }
    }

    fun validateDate(newValue: String) : Int? {
        return when {
            newValue.isEmpty()  ->  R.string.value_empty
            newValue.isBlank()  ->  R.string.value_blank
            newValue.length != 10 ->  R.string.date_format
            else -> null
        }
    }

    fun validateDuration(newValue: Int) : Int? {
        return when {
            newValue < 1 ->  R.string.duration_too_short
            newValue > 24 ->  R.string.duration_too_long
            else -> null
        }

    }

    fun validateVehicle(newValue: Vehicle?) : Int? {
        return null
    }

    fun validateDriver(newValue: Driver?) : Int? {
        return null
    }

    fun validatePassengers(newValue: List<Driver>) : Int? {
        return null
    }

    fun validateShift () : Boolean {
        if (validateLocation(shift.shift.location) != null) return false
        if (validateDate(shift.shift.startDate) != null) return false
        if (validateDate(shift.shift.endDate) != null) return false
        return true
    }
}