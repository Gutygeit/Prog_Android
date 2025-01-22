package fr.uha.hassenforder.team.ui.driver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.android.ui.app.UITitleBuilder
import fr.uha.hassenforder.android.ui.app.UITitleState
import fr.uha.hassenforder.android.ui.field.FieldWrapper
import fr.uha.hassenforder.android.viewmodel.Result
import fr.uha.hassenforder.team.model.Driver
import fr.uha.hassenforder.team.model.License
import fr.uha.hassenforder.team.repository.DriverRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DriverViewModel @Inject constructor(
    private val repository: DriverRepository
): ViewModel() {

    private val _id: MutableStateFlow<Long> = MutableStateFlow(0)

    private val _firstnameState = MutableStateFlow(FieldWrapper<String>())
    private val _lastnameState = MutableStateFlow(FieldWrapper<String>())
    private val _phoneState = MutableStateFlow(FieldWrapper<String>())
    private val _licenseState = MutableStateFlow(FieldWrapper<License>())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _initialDriverState: StateFlow<Result<Driver>> = _id
        .flatMapLatest { id -> repository.getDriverById(id) }
        .map {
                driver -> if (driver != null) {
            _firstnameState.value = fieldBuilder.buildFirstname(driver.firstname)
            _lastnameState.value = fieldBuilder.buildLastname(driver.lastname)
            _phoneState.value = fieldBuilder.buildPhone(driver.phone)
            _licenseState.value = fieldBuilder.buildLicense(driver.license)
            Result.Success(content = driver)
        } else {
            Result.Error()
        }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Result.Loading)

    @Suppress("UNCHECKED_CAST")
    data class UIState(
        val args: Array<FieldWrapper<out Any>>,
    ) {
        val firstnameState : FieldWrapper<String> = args[0] as FieldWrapper<String>
        val lastnameState : FieldWrapper<String> = args[1] as FieldWrapper<String>
        val phoneState : FieldWrapper<String> = args[2] as FieldWrapper<String>
        val licenseState : FieldWrapper<License> = args[3] as FieldWrapper<License>
    }

    val uiState : StateFlow<Result<UIState>> = combine(
        _firstnameState, _lastnameState, _phoneState, _licenseState,
    ) { args : Array<FieldWrapper<out Any>> -> Result.Success(UIState(args)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.Loading)

    private class FieldBuilder (private val validator : DriverUIValidator) {
        fun buildFirstname(newValue: String): FieldWrapper<String> {
            val errorId : Int? = validator.validateFirstnameChange(newValue)
            return FieldWrapper(newValue, errorId)
        }
        fun buildLastname(newValue: String): FieldWrapper<String> {
            val errorId : Int? = validator.validateLastnameChange(newValue)
            return FieldWrapper(newValue, errorId)
        }
        fun buildPhone(newValue: String): FieldWrapper<String> {
            val errorId : Int? = validator.validatePhoneChange(newValue)
            return FieldWrapper(newValue, errorId)
        }
        fun buildLicense(newValue: License?): FieldWrapper<License> {
            val errorId : Int? = validator.validateLicenseChange(newValue)
            return FieldWrapper(newValue, errorId)
        }
    }

    private val fieldBuilder = FieldBuilder(DriverUIValidator(uiState))

    val titleBuilder = UITitleBuilder()

    val uiTitleState : StateFlow<UITitleState> = titleBuilder.uiTitleState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UITitleState()
    )

    private fun isModified (initial: Result<Driver>, fields : Result<UIState>): Boolean? {
        if (initial !is Result.Success) return null
        if (fields !is Result.Success) return null
        if (fields.content.firstnameState.value != initial.content.firstname) return true
        if (fields.content.lastnameState.value != initial.content.lastname) return true
        if (fields.content.phoneState.value != initial.content.phone) return true
        if (fields.content.licenseState.value != initial.content.license) return true
        return false
    }

    private fun hasError (fields : Result<UIState>): Boolean? {
        if (fields !is Result.Success) return null
        return fields.content.args.any { it.errorId != null }
    }

    init {
        combine(_initialDriverState, uiState) { i, s ->
            titleBuilder.setModified(isModified(i, s))
            titleBuilder.setError(hasError(s))
        }.launchIn(viewModelScope)
    }

    sealed class UIEvent {
        data class FirstnameChanged(val newValue: String): UIEvent()
        data class LastnameChanged(val newValue: String): UIEvent()
        data class PhoneChanged(val newValue: String): UIEvent()
        data class GenderChanged(val newValue: License): UIEvent()
    }

    fun send (uiEvent : UIEvent) {
        viewModelScope.launch {
            when (uiEvent) {
                is UIEvent.FirstnameChanged -> _firstnameState.value =
                    fieldBuilder.buildFirstname(uiEvent.newValue)

                is UIEvent.LastnameChanged -> _lastnameState.value =
                    fieldBuilder.buildLastname(uiEvent.newValue)

                is UIEvent.PhoneChanged -> _phoneState.value =
                    fieldBuilder.buildPhone(uiEvent.newValue)

                is UIEvent.GenderChanged -> _licenseState.value =
                    fieldBuilder.buildLicense(uiEvent.newValue)

            }
        }
    }

    fun edit (did : Long) = viewModelScope.launch {
        _id.value = did
    }

    fun create(driver: Driver) = viewModelScope.launch {
        val did : Long = repository.create(driver)
        _id.value = did
    }

    fun save() = viewModelScope.launch {
        if (_initialDriverState.value !is Result.Success) return@launch
        if (uiState.value !is Result.Success) return@launch
        //val oldDriver = _initialDriverState.value as Result.Success
        val driver = Driver (
            _id.value,
            _firstnameState.value.value!!,
            _lastnameState.value.value!!,
            _phoneState.value.value!!,
            _licenseState.value.value!!
        )
        repository.update(driver)
    }

}