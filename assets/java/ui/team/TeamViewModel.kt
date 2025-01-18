package fr.uha.hassenforder.team.ui.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.android.ui.app.UITitleBuilder
import fr.uha.hassenforder.android.ui.app.UITitleState
import fr.uha.hassenforder.android.ui.field.FieldWrapper
import fr.uha.hassenforder.android.viewmodel.Result
import fr.uha.hassenforder.team.model.Comparators
import fr.uha.hassenforder.team.model.FullTeam
import fr.uha.hassenforder.team.model.Person
import fr.uha.hassenforder.team.model.Team
import fr.uha.hassenforder.team.repository.TeamRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor (
    private val repository : TeamRepository
): ViewModel() {

    private val _teamId: MutableStateFlow<Long> = MutableStateFlow(0)

    private val _nameState = MutableStateFlow(FieldWrapper<String>())
    private val _startDayState = MutableStateFlow(FieldWrapper<Date>())
    private val _durationState = MutableStateFlow(FieldWrapper<Int>())
    private val _leaderState = MutableStateFlow(FieldWrapper<Person>())
    private val _membersState = MutableStateFlow(FieldWrapper<List<Person>>())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _initialTeamState: StateFlow<Result<FullTeam>> = _teamId
        .flatMapLatest { id -> repository.getTeamById(id) }
        .map { team ->
            if (team != null) {
                _nameState.value = fieldBuilder.buildName(team.team.name)
                _startDayState.value = fieldBuilder.buildStartDay(team.team.startDay)
                _durationState.value = fieldBuilder.buildDuration(team.team.duration)
                _leaderState.value = fieldBuilder.buildLeader(team.leader)
                _membersState.value = fieldBuilder.buildMembers(team.members)
                Result.Success(content = team)
            } else {
                Result.Error()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Result.Loading)

    private val _updateLeaderId: MutableSharedFlow<Long> = MutableSharedFlow(0)
    private val _addMemberId: MutableSharedFlow<Long> = MutableSharedFlow(0)
    private val _delMemberId: MutableSharedFlow<Long> = MutableSharedFlow(0)

    @Suppress("UNCHECKED_CAST")
    data class UIState(
        private val args: Array<FieldWrapper<out Any>>,
    ) {
        val name: FieldWrapper<String> = args[0] as FieldWrapper<String>
        val startDay: FieldWrapper<Date> = args[1] as FieldWrapper<Date>
        val duration: FieldWrapper<Int> = args[2] as FieldWrapper<Int>
        val leader: FieldWrapper<Person> = args[3] as FieldWrapper<Person>
        val members: FieldWrapper<List<Person>> = args[4] as FieldWrapper<List<Person>>
    }

    val uiState : StateFlow<Result<UIState>> = combine(
        _nameState, _startDayState, _durationState, _leaderState, _membersState
    ) { args : Array<FieldWrapper<out Any>> -> Result.Success(UIState(args)) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Result.Loading)

    private class FieldBuilder(private val validator : TeamUIValidator) {
        fun buildName(newValue: String): FieldWrapper<String> {
            val errorId : Int? = validator.validateNameChange(newValue)
            return FieldWrapper(newValue, errorId)
        }

        fun buildStartDay(newValue: Date): FieldWrapper<Date> {
            val errorId : Int? = validator.validateStartDayChange(newValue)
            return FieldWrapper(newValue, errorId)
        }

        fun buildDuration(newValue: Int): FieldWrapper<Int> {
            val errorId : Int? = validator.validateDurationChange(newValue)
            return FieldWrapper(newValue, errorId)
        }

        fun buildLeader(newValue: Person?): FieldWrapper<Person> {
            val errorId : Int? = validator.validateLeaderChange(newValue)
            return FieldWrapper(newValue, errorId)
        }

        fun buildMembers(newValue: List<Person>): FieldWrapper<List<Person>> {
            val errorId : Int? = validator.validateMembersChange(newValue)
            return FieldWrapper(newValue, errorId)
        }
    }

    private val fieldBuilder = FieldBuilder(TeamUIValidator(uiState))

    val titleBuilder = UITitleBuilder()

    val uiTitleState : StateFlow<UITitleState> = titleBuilder.uiTitleState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UITitleState()
    )

    private fun isModified (initial: Result<FullTeam>, fields : Result<UIState>): Boolean? {
        if (initial !is Result.Success) return null
        if (fields !is Result.Success) return null
        if (fields.content.name.value != initial.content.team.name) return true
        if (fields.content.name.value != initial.content.team.name) return true
        if (fields.content.startDay.value != initial.content.team.startDay) return true
        if (fields.content.duration.value != initial.content.team.duration) return true
        if (! Comparators.shallowEqualsPerson(fields.content.leader.value, initial.content.leader)) return true
        if (! Comparators.shallowEqualsListPersons(fields.content.members.value, initial.content.members)) return true
        return false
    }

    private fun hasError (fields : Result<UIState>): Boolean? {
        if (fields !is Result.Success) return null
        if (fields.content.name.errorId != null) return true
        if (fields.content.startDay.errorId != null) return true
        if (fields.content.duration.errorId != null) return true
        if (fields.content.leader.errorId != null) return true
        if (fields.content.members.errorId != null) return true
        return false
    }

    init {
        combine(_initialTeamState, uiState) { i, s ->
            titleBuilder.setModified(isModified(i, s))
            titleBuilder.setError(hasError(s))
        }.launchIn(viewModelScope)
        _nameState.map {
            titleBuilder.setDocumentName(it.value)
        }.launchIn(viewModelScope)

        _updateLeaderId
            .flatMapLatest { id -> repository.getPersonById(id) }
            .map { p ->
                if (p != null) {
                    _leaderState.value = fieldBuilder.buildLeader(p)
                } else {
                    _leaderState.value = fieldBuilder.buildLeader(null)
                }
            }
            .launchIn(viewModelScope)

        _addMemberId
            .flatMapLatest { id -> repository.getPersonById(id) }
            .map {
                    p -> if (p != null) {
                val mm : MutableList<Person> = _membersState.value.value?.toMutableList() ?: mutableListOf()
                mm.add(p)
                _membersState.value = fieldBuilder.buildMembers(mm)
            }
            }
            .launchIn(viewModelScope)

        _delMemberId
            .map {
                val mm: MutableList<Person> = mutableListOf()
                _membersState.value.value?.forEach { m ->
                    if (m.pid != it) mm.add(m)
                }
                _membersState.value = fieldBuilder.buildMembers(mm)
            }
            .launchIn(viewModelScope)
    }

    sealed class UIEvent {
        data class NameChanged(val newValue: String): UIEvent()
        data class StartDayChanged(val newValue: Date): UIEvent()
        data class DurationChanged(val newValue: Int): UIEvent()
        data class LeaderChanged(val newValue: Long?): UIEvent()
        data class MemberAdded(val newValue: Long): UIEvent()
        data class MemberDeleted(val newValue: Person): UIEvent()
    }

    fun send (uiEvent : UIEvent) {
        viewModelScope.launch {
            when (uiEvent) {
                is UIEvent.NameChanged -> _nameState.value = fieldBuilder.buildName(uiEvent.newValue)
                is UIEvent.StartDayChanged -> _startDayState.value =
                    fieldBuilder.buildStartDay(uiEvent.newValue)

                is UIEvent.DurationChanged -> _durationState.value =
                    fieldBuilder.buildDuration(uiEvent.newValue)

                is UIEvent.LeaderChanged -> {
                    if (uiEvent.newValue != null) _updateLeaderId.emit(uiEvent.newValue)
                    else _leaderState.value = fieldBuilder.buildLeader(null)
                }

                is UIEvent.MemberAdded -> _addMemberId.emit(uiEvent.newValue)
                is UIEvent.MemberDeleted -> _delMemberId.emit(uiEvent.newValue.pid)
            }
        }
    }

    fun edit(pid: Long) = viewModelScope.launch {
        _teamId.value = pid
    }

    fun create(team: Team) = viewModelScope.launch {
        val pid : Long = repository.createTeam(team)
        _teamId.value = pid
    }

    fun save() = viewModelScope.launch {
        if (_initialTeamState.value !is Result.Success) return@launch
        val oldTeam = _initialTeamState.value as Result.Success
        val team = FullTeam (
            Team (
                tid = _teamId.value,
                name = _nameState.value.value!!,
                startDay = _startDayState.value.value!!,
                duration = _durationState.value.value!!,
                leaderId = _leaderState.value.value?.pid ?: 0
            ),
            leader = _leaderState.value.value,
            members = _membersState.value.value!!
        )
        repository.saveTeam(oldTeam.content, team)
    }

}
