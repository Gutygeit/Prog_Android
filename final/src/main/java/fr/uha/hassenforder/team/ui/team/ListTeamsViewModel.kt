package fr.uha.hassenforder.team.ui.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.android.viewmodel.Result
import fr.uha.hassenforder.team.model.Team
import fr.uha.hassenforder.team.repository.TeamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListTeamsViewModel @Inject constructor (
    private val repository: TeamRepository
): ViewModel() {

    private val _teams: Flow<List<Team>> = repository.getAll()

    data class UIState (
        val teams : List<Team>
    )

    val uiState : StateFlow<Result<UIState>> = _teams
        .map { p -> Result.Success(UIState(p)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.Loading
        )

    sealed class UIEvent {
        data class OnDelete(val newValue: Team): UIEvent()
    }

    fun send (uiEvent : UIEvent) {
        viewModelScope.launch {
            when (uiEvent) {
                is UIEvent.OnDelete -> delete(uiEvent.newValue)
            }
        }
    }

    private fun delete(Team: Team) = viewModelScope.launch {
        repository.delete(Team)
    }

}