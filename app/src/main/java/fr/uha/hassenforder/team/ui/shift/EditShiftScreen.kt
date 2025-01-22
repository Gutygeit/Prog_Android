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

@Destination<RootGraph>
@Composable
fun EditShiftScreen (
    vm : ShiftViewModel = hiltViewModel(),
    navigator : DestinationsNavigator,
    sid : Long,
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect (Unit){
        vm.edit(sid, sid)
        vm.titleBuilder.setScreenNameId(R.string.edit_shift)
    }
    StateScreen(state = uiState) {
        content ->
        SuccessShiftScreen(content, { vm.send(it) })
    }
}