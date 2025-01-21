package fr.uha.hassenforder.team.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.team.database.FeedDatabase
import fr.uha.hassenforder.team.database.ShiftDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val database: ShiftDatabase,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel(){

    fun onClear () = viewModelScope.launch {
        withContext(dispatcher) {
            FeedDatabase(database).clear()
        }
    }

    fun onFill () = viewModelScope.launch {
        withContext(dispatcher) {
            FeedDatabase(database).populate(0)
        }
    }
}
