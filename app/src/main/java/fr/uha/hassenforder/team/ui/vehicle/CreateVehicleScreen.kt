package fr.uha.hassenforder.team.ui.vehicle

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.hassenforder.android.ui.StateScreen
import fr.uha.hassenforder.android.ui.app.AppMenuEntry
import fr.uha.hassenforder.android.ui.app.AppTopBar
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Vehicle
import fr.uha.hassenforder.team.model.VehicleStatus

@Destination<RootGraph>
@Composable
fun CreateVehicleScreenDestination(
    vm: VehicleViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val uiTitleState by vm.uiTitleState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        /*vm.create(
            Vehicle(0, "", "", 0, VehicleStatus.AVAILABLE)
        )*/
        vm.titleBuilder.setScreenNameId(R.string.create_vehicle)
    }

    val menuEntries = listOf(
        AppMenuEntry.ActionEntry(
            titleId = R.string.save,
            icon = Icons.Filled.Save,
            //enabled = { uiTitleState.isSavable ?: false },
            listener = {
                vm.create(
                    Vehicle(0, "", "", 0, VehicleStatus.AVAILABLE)
                )
                vm.save()
                navigator.popBackStack()
            }
        )
    )


    Scaffold(
        topBar = {
            AppTopBar(uiTitleState = uiTitleState, navigator, menuEntries = menuEntries)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), // Marges uniformes pour plus de lisibilité
                verticalArrangement = Arrangement.spacedBy(20.dp), // Espacement uniforme entre les éléments
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Zone de contenu principal
                StateScreen(state = uiState) { content ->
                    SuccessVehicleScreen(content) { vm.send(it) }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bouton "Save" aligné en bas, mais redondant avec l'icône du menu
                /* Uncomment si un bouton visuel est requis en plus
                Button(
                    onClick = {
                        vm.save()
                        navigator.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.save))
                }
                */
            }
        }
    }
}
