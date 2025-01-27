package fr.uha.hassenforder.team.ui.shift

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.hassenforder.android.ui.StateScreen
import fr.uha.hassenforder.android.ui.app.AppTopBar
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Shift
import fr.uha.hassenforder.team.ui.vehicle.SuccessVehicleScreen

@Destination<RootGraph>
@Composable
fun CreateShiftScreen (
   vm : ShiftViewModel = hiltViewModel(),
   navigator: DestinationsNavigator
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val uiTitleState by vm.uiTitleState.collectAsStateWithLifecycle()


    LaunchedEffect(Unit) {
        vm.create(
            Shift(0, "", "", 0, 0, 0)
        )
        vm.titleBuilder.setScreenNameId(R.string.create_shift)
    }

    Scaffold (
        modifier = Modifier.padding(top = 30.dp),
        topBar = {
            AppTopBar(uiTitleState = uiTitleState, navigator)
        }
    ) { innerPadding ->
        Column (
            modifier = Modifier.padding(innerPadding)
        ) {
            StateScreen(state = uiState) {
                    content ->
                SuccessShiftScreen (content) { vm.send(it) }
            }
        }
    }
}
