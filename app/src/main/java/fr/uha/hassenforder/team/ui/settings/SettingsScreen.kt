package fr.uha.hassenforder.team.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import fr.uha.hassenforder.android.ui.app.AppTopBar
import fr.uha.hassenforder.android.ui.app.UITitleState
import fr.uha.hassenforder.team.R

@Destination<RootGraph>
@Composable
fun SettingsScreen (
    vm : SettingsViewModel = hiltViewModel(),
    //navigator: DestinationsNavigator
) {
    Scaffold (
        topBar = { AppTopBar(UITitleState(screenNameId = R.string.settings)) },
    ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                Button(
                    onClick = { vm.onClear() }
                ) {
                    Text("Nettoyer")
                }
                Button(
                    onClick = { vm.onFill() }
                ) {
                    Text("Remplir")
                }
            }
        }
}

