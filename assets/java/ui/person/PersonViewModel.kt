package fr.uha.hassenforder.team.ui.person

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.android.ui.app.UITitleBuilder
import fr.uha.hassenforder.android.ui.app.UITitleState
import fr.uha.hassenforder.android.ui.field.FieldWrapper
import fr.uha.hassenforder.android.viewmodel.Result
import fr.uha.hassenforder.team.model.Gender
import fr.uha.hassenforder.team.model.Person
import fr.uha.hassenforder.team.repository.PersonRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor (
    private val repository: PersonRepository
): ViewModel() {

    private val _id: MutableStateFlow<Long> = MutableStateFlow(0)

    private val _firstnameState = MutableStateFlow(FieldWrapper<String>())
    private val _lastnameState = MutableStateFlow(FieldWrapper<String>())
    private val _phoneState = MutableStateFlow(FieldWrapper<String>())
    private val _genderState = MutableStateFlow(FieldWrapper<Gender>())
    private val _pictureState = MutableStateFlow(FieldWrapper<Uri>())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _initialPersonState: StateFlow<Result<Person>> = _id
        .flatMapLatest { id -> repository.getPersonById(id) }
        .map {
                person -> if (person != null) {
            _firstnameState.value = fieldBuilder.buildFirstname(person.firstname)
            _lastnameState.value = fieldBuilder.buildLastname(person.lastname)
            _phoneState.value = fieldBuilder.buildPhone(person.phone)
            _genderState.value = fieldBuilder.buildGender(person.gender)
            _pictureState.value = fieldBuilder.buildPicture(person.picture)
            Result.Success(content = person)
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
        val genderState : FieldWrapper<Gender> = args[3] as FieldWrapper<Gender>
        val pictureState : FieldWrapper<Uri> = args[4] as FieldWrapper<Uri>
    }

    val uiState : StateFlow<Result<UIState>> = combine(
        _firstnameState, _lastnameState, _phoneState, _genderState, _pictureState,
    ) { args : Array<FieldWrapper<out Any>> -> Result.Success(UIState(args)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.Loading)

    private class FieldBuilder (private val validator : PersonUIValidator) {
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
        fun buildGender(newValue: Gender?): FieldWrapper<Gender> {
            val errorId : Int? = validator.validateGenderChange(newValue)
            return FieldWrapper(newValue, errorId)
        }
        fun buildPicture(newValue: Uri?): FieldWrapper<Uri> {
            val errorId : Int? = validator.validatePictureChange(newValue)
            return FieldWrapper(newValue, errorId)
        }
    }

    private val fieldBuilder = FieldBuilder(PersonUIValidator(uiState))

    val titleBuilder = UITitleBuilder()

    val uiTitleState : StateFlow<UITitleState> = titleBuilder.uiTitleState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UITitleState()
    )

    private fun isModified (initial: Result<Person>, fields : Result<UIState>): Boolean? {
        if (initial !is Result.Success) return null
        if (fields !is Result.Success) return null
        if (fields.content.firstnameState.value != initial.content.firstname) return true
        if (fields.content.lastnameState.value != initial.content.lastname) return true
        if (fields.content.phoneState.value != initial.content.phone) return true
        if (fields.content.genderState.value != initial.content.gender) return true
        if (fields.content.pictureState.value != initial.content.picture) return true
        return false
    }

    private fun hasError (fields : Result<UIState>): Boolean? {
        if (fields !is Result.Success) return null
        return fields.content.args.any { it.errorId != null }
    }

    init {
        combine(_initialPersonState, uiState) { i, s ->
            titleBuilder.setModified(isModified(i, s))
            titleBuilder.setError(hasError(s))
        }.launchIn(viewModelScope)
    }

    sealed class UIEvent {
        data class FirstnameChanged(val newValue: String): UIEvent()
        data class LastnameChanged(val newValue: String): UIEvent()
        data class PhoneChanged(val newValue: String): UIEvent()
        data class GenderChanged(val newValue: Gender): UIEvent()
        data class PictureChanged(val newValue: Uri?): UIEvent()
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

                is UIEvent.GenderChanged -> _genderState.value =
                    fieldBuilder.buildGender(uiEvent.newValue)

                is UIEvent.PictureChanged -> _pictureState.value =
                    fieldBuilder.buildPicture(uiEvent.newValue)

                else -> {}
            }
        }
    }

    fun edit (pid : Long) = viewModelScope.launch {
        _id.value = pid
    }

    fun create(person: Person) = viewModelScope.launch {
        val pid : Long = repository.create(person)
        _id.value = pid
    }

    fun save() = viewModelScope.launch {
        if (_initialPersonState.value !is Result.Success) return@launch
        if (uiState.value !is Result.Success) return@launch
        val oldPerson = _initialPersonState.value as Result.Success
        val person = Person (
            _id.value,
            _firstnameState.value.value!!,
            _lastnameState.value.value!!,
            _phoneState.value.value!!,
            _genderState.value.value!!,
            _pictureState.value.value
        )
        repository.update(oldPerson.content, person)
    }

}
