package fr.uha.hassenforder.team.ui.vehicle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.android.ui.app.UITitleBuilder
import fr.uha.hassenforder.android.ui.app.UITitleState
import fr.uha.hassenforder.android.ui.field.FieldWrapper
import fr.uha.hassenforder.team.model.Vehicle
import fr.uha.hassenforder.team.model.VehicleStatus
import fr.uha.hassenforder.team.repository.VehicleRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import fr.uha.hassenforder.android.viewmodel.Result
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


@HiltViewModel
class VehicleViewModel @Inject constructor(
    private val repository:  VehicleRepository
) : ViewModel(){

    private val _id: MutableStateFlow<Long> = MutableStateFlow(0)

    private val _brandAndModelState = MutableStateFlow(FieldWrapper<String>())
    private val _matriculationState = MutableStateFlow(FieldWrapper<String>())
    private val _kilometersState = MutableStateFlow(FieldWrapper<Int>())
    private val _vehicleStatusState = MutableStateFlow(FieldWrapper<VehicleStatus>())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _initialVehicleState: StateFlow<Result<Vehicle>> = _id
        .flatMapLatest { id -> repository.getVehicleById(id) }
        .map {
            vehicle -> if (vehicle != null) {
            _brandAndModelState.value = fieldBuilder.buildBrandAndModel(vehicle.brandAndModel)
            _matriculationState.value = fieldBuilder.buildMatriculation(vehicle.matriculation)
            _kilometersState.value = fieldBuilder.buildKilometers(vehicle.kilometers)
            _vehicleStatusState.value = fieldBuilder.buildVehicleStatus(vehicle.status)
            Result.Success(content = vehicle)
        } else {
            Result.Error()
        }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Result.Loading)

    @Suppress("UNCHECKED_CAST")
    data class UIState(
        val args: Array<FieldWrapper<out Any>>,
    ) {
        val brandAndModelState : FieldWrapper<String> = args[0] as FieldWrapper<String>
        val matriculationState : FieldWrapper<String> = args[1] as FieldWrapper<String>
        val kilometersState : FieldWrapper<Int> = args[2] as FieldWrapper<Int>
        val vehicleStatusState : FieldWrapper<VehicleStatus> = args[3] as FieldWrapper<VehicleStatus>
    }

    val uiState : StateFlow<Result<UIState>> = combine(
        _brandAndModelState, _matriculationState, _kilometersState, _vehicleStatusState,
    ) { args : Array<FieldWrapper<out Any>> -> Result.Success(UIState(args)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.Loading
        )

    private class FieldBuilder (private val validator : VehicleUIValidator) {
        fun buildBrandAndModel(newValue: String): FieldWrapper<String> {
            val errorId : Int? = validator.validateBrandAndModelChange(newValue)
            return FieldWrapper(newValue, errorId)
        }
        fun buildMatriculation(newValue: String): FieldWrapper<String> {
            val errorId : Int? = validator.validateMatriculationChange(newValue)
            return FieldWrapper(newValue, errorId)
        }
        fun buildKilometers(newValue: Int): FieldWrapper<Int> {
            val errorId : Int? = validator.validateKilometersChange(newValue)
            return FieldWrapper(newValue, errorId)
        }
        fun buildVehicleStatus(newValue: VehicleStatus): FieldWrapper<VehicleStatus> {
            val errorId : Int? = validator.validateVehicleStatusChange(newValue)
            return FieldWrapper(newValue, errorId)
        }
    }

    private val fieldBuilder = FieldBuilder(VehicleUIValidator(uiState))

    val titleBuilder = UITitleBuilder()

    val uiTitleState : StateFlow<UITitleState> = titleBuilder.uiTitleState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UITitleState()
    )

    private fun isModified (initial: Result<Vehicle>, fields : Result<UIState>): Boolean? {
        if (initial !is Result.Success) return null
        if (fields !is Result.Success) return null
        if (fields.content.brandAndModelState.value != initial.content.brandAndModel) return true
        if (fields.content.matriculationState.value != initial.content.matriculation) return true
        if (fields.content.kilometersState.value != initial.content.kilometers) return true
        if (fields.content.vehicleStatusState.value != initial.content.status) return true
        return false
    }

    private fun hasError (fields : Result<UIState>): Boolean? {
        if (fields !is Result.Success) return null
        return fields.content.args.any { it.errorId != null }
    }

    init {
        combine(_initialVehicleState, uiState) { i, s ->
            titleBuilder.setModified(isModified(i, s))
            titleBuilder.setError(hasError(s))
        }.launchIn(viewModelScope)
    }

    sealed class UIEvent {
        data class BrandAndModelChanged(val newValue: String): UIEvent()
        data class MatriculationChanged(val newValue: String): UIEvent()
        data class KilometersChanged(val newValue: Int): UIEvent()
        data class VehicleStatusChanged(val newValue: VehicleStatus): UIEvent()
    }

    fun send (uiEvent: UIEvent) {
        viewModelScope.launch {
            when (uiEvent) {
                is UIEvent.BrandAndModelChanged -> _brandAndModelState.value = fieldBuilder.buildBrandAndModel(uiEvent.newValue)
                is UIEvent.MatriculationChanged -> _matriculationState.value = fieldBuilder.buildMatriculation(uiEvent.newValue)
                is UIEvent.KilometersChanged -> _kilometersState.value = fieldBuilder.buildKilometers(uiEvent.newValue)
                is UIEvent.VehicleStatusChanged -> _vehicleStatusState.value = fieldBuilder.buildVehicleStatus(uiEvent.newValue)
                else -> {}
            }
        }
    }

    fun edit (vid : Long) = viewModelScope.launch {
        _id.value = vid
    }

    fun create(vehicle: Vehicle) = viewModelScope.launch {
        val vid : Long = repository.create(vehicle)
        _id.value = vid
    }

    fun save() = viewModelScope.launch {
        if (_initialVehicleState.value is Result.Success) return@launch
        if (uiState.value !is Result.Success) return@launch
        val oldVehicle  = _initialVehicleState.value as Result.Success
        val vehicle = Vehicle (
            _id.value,
            _brandAndModelState.value.value!!,
            _matriculationState.value.value!!,
            _kilometersState.value.value!!,
            _vehicleStatusState.value.value!!
        )
        repository.update(vehicle)
    }
}