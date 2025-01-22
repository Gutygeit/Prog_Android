package fr.uha.hassenforder.team.ui.shift

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.android.viewmodel.Result
import fr.uha.hassenforder.team.model.Shift
import fr.uha.hassenforder.team.repository.ShiftRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ListShiftsViewModel @Inject constructor (
    private val repository: ShiftRepository
) : ViewModel() {

    private val _shifts : Flow<List<Shift>> = repository.getAll()

    data class UIState (
        val shifts : List<Shift>
    )

    val uiState : StateFlow<Result<UIState>> = _shifts
        .map { list : List<Shift> ->
            Result.Success(UIState(list))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.Loading
        )

    sealed class UIEvent {
        data class OnDelete(val shift: Shift) : UIEvent()
    }

    fun send (uiEvent : UIEvent) {
        viewModelScope.launch {
            when (uiEvent) {
                is UIEvent.OnDelete -> onDelete(uiEvent.shift)
            }
        }
    }

    fun onDelete (shift: Shift) = viewModelScope.launch {
        repository.delete(shift)
    }

}