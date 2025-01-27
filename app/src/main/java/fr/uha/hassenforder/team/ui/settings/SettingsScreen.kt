package fr.uha.hassenforder.team.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import fr.uha.hassenforder.android.ui.app.AppTopBar
import fr.uha.hassenforder.android.ui.app.UITitleState
import fr.uha.hassenforder.team.R

@Destination<RootGraph>
@Composable
fun SettingsScreen(
    vm: SettingsViewModel = hiltViewModel(),
    //navigator: DestinationsNavigator
) {
    Scaffold(
        topBar = { AppTopBar(UITitleState(screenNameId = R.string.settings)) },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp) // Espacement vertical uniforme
            ) {
                Button(
                    onClick = { vm.onClear() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Nettoyer")
                }
                Button(
                    onClick = { vm.onFill() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Remplir")
                }
            }
        }
    }
}
