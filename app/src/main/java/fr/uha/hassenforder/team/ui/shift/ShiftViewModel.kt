package fr.uha.hassenforder.team.ui.shift

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.android.ui.app.UITitleBuilder
import fr.uha.hassenforder.android.ui.app.UITitleState
import fr.uha.hassenforder.android.ui.field.FieldWrapper
import fr.uha.hassenforder.android.viewmodel.Result
import fr.uha.hassenforder.team.database.ShiftUpdateDTO
import fr.uha.hassenforder.team.model.Driver
import fr.uha.hassenforder.team.model.FullShift
import fr.uha.hassenforder.team.model.Shift
import fr.uha.hassenforder.team.model.Vehicle
import fr.uha.hassenforder.team.repository.ShiftRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShiftViewModel @Inject constructor(
    private val repository: ShiftRepository
): ViewModel() {

    private val _id: MutableStateFlow<Long> = MutableStateFlow(0)

    data class UIState(
        val location: FieldWrapper<String>,
        val startDate: FieldWrapper<String>,
        val endDate: FieldWrapper<String>,
        val vehicle: FieldWrapper<Vehicle?>,
        val driver: FieldWrapper<Driver?>,
        val passengers: FieldWrapper<List<Driver>>,
        val shift: FullShift,
    ) {
        companion object {
            fun create(shift: FullShift): UIState {
                val validator = ShiftUIValidator(shift)
                val location = FieldWrapper(
                    shift.shift.location,
                    validator.validateLocation(shift.shift.location)
                )
                val startDate = FieldWrapper(shift.shift.startDate, validator.validateDate(shift.shift.startDate))
                val endDate = FieldWrapper(shift.shift.endDate, validator.validateDate(shift.shift.endDate))
                val vehicle: FieldWrapper<Vehicle?> =
                    FieldWrapper(shift.vehicle, validator.validateVehicle(shift.vehicle))
                val driver: FieldWrapper<Driver?> =
                    FieldWrapper(shift.driver, validator.validateDriver(shift.driver))
                val passengers: FieldWrapper<List<Driver>> =
                    FieldWrapper(shift.passengers, validator.validatePassengers(shift.passengers))
                return UIState(location, startDate, endDate, vehicle, driver, passengers, shift)
            }
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<Result<UIState>> = _id
        .flatMapLatest { id -> repository.getShiftById(id) }
        .map { shift ->
            if (shift != null)
                Result.Success(UIState.create(shift))
            else Result.Error()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.Loading
        )

    val titleBuilder = UITitleBuilder()

    val uiTitleState: StateFlow<UITitleState> = titleBuilder.uiTitleState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UITitleState()
    )

    sealed class UIEvent {
        data class LocationChanged(val newValue: String) : UIEvent()
        data class StartDateChanged(val newValue: String) : UIEvent()
        data class EndDateChanged(val newValue: String) : UIEvent()
        data class VehicleChanged(val newValue: Vehicle?) : UIEvent()
        data class DriverChanged(val newValue: Driver?) : UIEvent()
        data class AddMember(val newValue: Driver) : UIEvent()
        data class RemoveMember(val newValue: Driver) : UIEvent()
    }

    fun send(uiEvent: UIEvent) {
        viewModelScope.launch {
            if (uiState.value !is Result.Success) return@launch
            val sid = (uiState.value as Result.Success<UIState>).content.shift.shift.sid
            when (uiEvent) {
                is UIEvent.LocationChanged ->
                    repository.update(ShiftUpdateDTO.Location(sid, uiEvent.newValue))

                is UIEvent.StartDateChanged ->
                    repository.update(ShiftUpdateDTO.StartDate(sid, uiEvent.newValue))

                is UIEvent.EndDateChanged ->
                    repository.update(ShiftUpdateDTO.EndDate(sid, uiEvent.newValue))

                is UIEvent.VehicleChanged ->
                    repository.update(ShiftUpdateDTO.Vehicle(sid, uiEvent.newValue?.vid ?: 0))

                is UIEvent.DriverChanged ->
                    repository.update(ShiftUpdateDTO.Driver(sid, uiEvent.newValue?.did ?: 0))

                is UIEvent.AddMember ->
                    repository.addMember(sid, uiEvent.newValue)

                is UIEvent.RemoveMember ->
                    repository.removeMember(sid, uiEvent.newValue)

            }
        }
    }

    fun edit(vid: Long, did: Long) = viewModelScope.launch {
        _id.value = vid
        _id.value = did
    }


    fun create(shift: Shift) = viewModelScope.launch {
        val id: Long = repository.create(shift)
        _id.value = id
    }

}

