package fr.uha.hassenforder.team.ui.vehicle

import fr.uha.hassenforder.android.viewmodel.Result
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.VehicleStatus
import kotlinx.coroutines.flow.StateFlow

class VehicleUIValidator(private val uiState: StateFlow<Result<VehicleViewModel.UIState>>) {

    fun validateBrandAndModelChange(newValue: String) : Int? {
        return when {
            newValue.isEmpty()  ->  R.string.value_empty
            newValue.isBlank()  ->  R.string.value_blank
            newValue.length < 2 ->  R.string.value_too_short
            newValue.length > 30 ->  R.string.value_too_long
            else -> null
        }
    }

    fun validateMatriculationChange(newValue: String) : Int? {
        return when {
            newValue.isEmpty()  ->  R.string.value_empty
            newValue.isBlank()  ->  R.string.value_blank
            newValue.length != 9 -> R.string.invalid_matriculation_length

            else -> null
        }
    }

    fun validateKilometersChange(newValue: Int) : Int? {
        return when {
            newValue < 0 ->  R.string.value_negative
            else -> null
        }
    }

    fun validateVehicleStatusChange(newValue: VehicleStatus?) : Int? {
        return when {
            newValue == null ->  R.string.status_unknown
            else -> null
        }
    }
}