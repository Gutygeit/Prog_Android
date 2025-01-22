package fr.uha.hassenforder.team.ui.shift

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.hassenforder.android.ui.StateScreen
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Shift

@Destination<RootGraph>
@Composable
fun CreateShiftScreen (
   vm : ShiftViewModel = hiltViewModel(),
   navigator: DestinationsNavigator
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.create(
            Shift(0, "Mulhouse", "12/12/2012", 5, 0, 0)
        )
        vm.titleBuilder.setScreenNameId(R.string.create_shift)
    }
    StateScreen(state = uiState) {
        content ->
        SuccessShiftScreen(content, { vm.send(it) })
    }
}