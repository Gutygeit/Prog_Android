package fr.uha.hassenforder.team.ui.driver

import fr.uha.hassenforder.android.viewmodel.Result
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.License
import kotlinx.coroutines.flow.StateFlow

class DriverUIValidator(private val uiState: StateFlow<Result<DriverViewModel.UIState>>) {

    fun validateFirstnameChange(newValue: String) : Int? {
        return when {
            newValue.isEmpty()  ->  R.string.value_empty
            newValue.isBlank()  ->  R.string.value_blank
            newValue.length < 3 ->  R.string.value_too_short
            newValue.length > 12 ->  R.string.value_too_long
            else -> null
        }
    }

    fun validateLastnameChange(newValue: String) : Int? {
        return when {
            newValue.isEmpty()  ->  R.string.value_empty
            newValue.isBlank()  ->  R.string.value_blank
            newValue.length < 3 ->  R.string.value_too_short
            newValue.length > 12 ->  R.string.value_too_long
            else -> null
        }
    }

    fun validatePhoneChange(newValue: String) : Int? {
        return if (newValue.startsWith("+")) {
            when {
                newValue.length < 12 ->  R.string.value_too_short
                newValue.length > 12 ->  R.string.value_too_long
                else -> R.string.phone_illegal
            }
        } else {
            when {
                newValue.length < 4 ->  R.string.value_too_short
                newValue.length > 10 ->  R.string.value_too_long
                newValue.length == 4 ->  null
                newValue.length == 10 ->  null
                else -> R.string.phone_illegal
            }
        }
    }

    fun validateLicenseChange(newValue: License?) : Int? {
        return when {
            newValue == null ->  R.string.license_must_set
            else -> null
        }
    }
}