package fr.uha.hassenforder.team.ui.team

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.hassenforder.android.ui.StateScreen
import fr.uha.hassenforder.android.ui.app.AppMenuEntry
import fr.uha.hassenforder.android.ui.app.AppTopBar
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Team

@Destination<RootGraph>
@Composable
fun CreateTeamScreen(
    vm: TeamViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val uiTitleState by vm.uiTitleState.collectAsStateWithLifecycle()
    val defaultDocumentName = stringResource(id = R.string.title_team)

    LaunchedEffect(Unit) {
        val team = Team()
        vm.create(team)
        vm.titleBuilder.setScreenNameId(R.string.title_create)
        vm.titleBuilder.setDefaultDocumentName(defaultDocumentName)
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
        topBar = { AppTopBar(uiTitleState, navigator, menuEntries) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            StateScreen(state = uiState) {
                SuccessTeamScreen(it, { vm.send(it) })
            }
        }
    }
}
