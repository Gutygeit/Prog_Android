package fr.uha.hassenforder.team.ui.driver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.android.viewmodel.Result
import fr.uha.hassenforder.team.model.Driver
import fr.uha.hassenforder.team.repository.DriverRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListDriversViewModel @Inject constructor (
    private val repository: DriverRepository
) : ViewModel() {

    private val _drivers : Flow<List<Driver>> = repository.getAll()

    data class UIState (
        val drivers : List<Driver>
    )

    val uiState : StateFlow<Result<UIState>> = _drivers
        .map { list : List<Driver> ->
            Result.Success(UIState(list))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.Loading
        )

    sealed class UIEvent {
        data class OnDelete(val driver: Driver) : UIEvent()
    }

    fun send (uiEvent : UIEvent) {
        viewModelScope.launch {
            when (uiEvent) {
                is ListDriversViewModel.UIEvent.OnDelete -> onDelete(uiEvent.driver)
            }
        }
    }

    fun onDelete (driver: Driver) = viewModelScope.launch {
        repository.delete(driver)
    }

}