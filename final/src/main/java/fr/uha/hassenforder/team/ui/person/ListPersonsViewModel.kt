package fr.uha.hassenforder.team.ui.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.android.viewmodel.Result
import fr.uha.hassenforder.team.model.Person
import fr.uha.hassenforder.team.model.PersonWithDetails
import fr.uha.hassenforder.team.repository.PersonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListPersonsViewModel @Inject constructor (
    private val repository: PersonRepository
): ViewModel() {

    private val _persons: Flow<List<PersonWithDetails>> = repository.getAllWithDetails()

    data class UIState (
        val persons : List<PersonWithDetails>
    )

    val uiState : StateFlow<Result<UIState>> = _persons
        .map { p -> Result.Success(UIState(p)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.Loading
        )

    sealed class UIEvent {
        data class OnDelete(val newValue: Person): UIEvent()
    }

    fun send (uiEvent : UIEvent) {
        viewModelScope.launch {
            when (uiEvent) {
                is UIEvent.OnDelete -> delete(uiEvent.newValue)
            }
        }
    }

    private fun delete(person: Person) = viewModelScope.launch {
        repository.delete(person)
    }

}