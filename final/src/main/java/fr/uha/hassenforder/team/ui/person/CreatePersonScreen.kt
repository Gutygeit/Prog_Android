package fr.uha.hassenforder.team.ui.person

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.hassenforder.android.ui.StateScreen
import fr.uha.hassenforder.android.ui.app.AppMenuEntry
import fr.uha.hassenforder.android.ui.app.AppTopBar
import fr.uha.hassenforder.android.ui.app.UITitleState
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.database.TeamDatabase
import fr.uha.hassenforder.team.model.Gender
import fr.uha.hassenforder.team.model.Person
import fr.uha.hassenforder.team.repository.PersonRepository
import kotlinx.coroutines.Dispatchers

@Destination<RootGraph>
@Composable
fun CreatePersonScreen (
    vm: PersonViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val uiTitleState by vm.uiTitleState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        val person = Person (firstname = "Michel", lastname = "Hassenforder", phone="0123456789", gender = Gender.BOY)
        vm.create(person)
        vm.titleBuilder.setScreenNameId(sid = R.string.title_person_create)
    }

    val menuEntries = listOf (
        AppMenuEntry.ActionEntry(
            titleId = R.string.save,
            icon = Icons.Filled.Save,
            enabled = { uiTitleState.isSavable ?: false },
            listener = { vm.save(); navigator.popBackStack() }
        ),
    )

    Scaffold(
        topBar = { AppTopBar(uiTitleState, menuEntries = menuEntries) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            StateScreen(state = uiState) {
                SuccessPersonScreen(it, { vm.send(it) })
            }
        }
    }
}
