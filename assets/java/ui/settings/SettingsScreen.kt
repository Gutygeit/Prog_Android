package fr.uha.hassenforder.team.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.hassenforder.android.ui.app.AppTopBar
import fr.uha.hassenforder.android.ui.app.UITitleState
import fr.uha.hassenforder.team.R

@Destination<RootGraph>
@Composable
fun SettingsScreen(
    vm: SettingsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    Scaffold(
        topBar = { AppTopBar(UITitleState(screenNameId = R.string.title_settings)) },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Button(onClick = { vm.populateDatabase(0) }) {
                Text(stringResource(id = R.string.populate))
            }
            Button(onClick = { vm.clearDatabase() }) {//, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(id = R.string.clear))
            }
        }
    }

}