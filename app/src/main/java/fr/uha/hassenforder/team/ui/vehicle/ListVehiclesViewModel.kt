package fr.uha.hassenforder.team.ui.vehicle

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.team.model.Vehicle
import fr.uha.hassenforder.team.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import fr.uha.hassenforder.android.viewmodel.Result
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ListVehiclesViewModel @Inject constructor(
    private val repository: VehicleRepository
) : ViewModel() {

    private val _vehicles : Flow<List<Vehicle>> = repository.getAll()

    data class UIState (
        val vehicles : List<Vehicle>
    )

    val uiState : StateFlow<Result<UIState>> = _vehicles
        .map { list : List<Vehicle> ->
            Result.Success(UIState(list))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.Loading
        )

    sealed class UIEvent {
        data class OnDelete(val vehicle: Vehicle) : UIEvent()
    }

    fun send (uiEvent : UIEvent) {
        viewModelScope.launch {
            when (uiEvent) {
                is ListVehiclesViewModel.UIEvent.OnDelete -> onDelete(uiEvent.vehicle)
            }
        }
    }

    fun onDelete (vehicle: Vehicle) = viewModelScope.launch {
        repository.delete(vehicle)
    }
}