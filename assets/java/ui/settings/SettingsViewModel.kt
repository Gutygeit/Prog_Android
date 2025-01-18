package fr.uha.hassenforder.team.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.team.database.FeedDatabase
import fr.uha.hassenforder.team.database.TeamDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor (
    private val db: TeamDatabase,
): ViewModel() {

    fun populateDatabase (mode : Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            FeedDatabase(db).populate(mode)
        }
    }

    fun clearDatabase () = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            FeedDatabase(db).clear()
        }
    }

}