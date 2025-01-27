package fr.uha.hassenforder.shift.ui.shift

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.hassenforder.android.ui.StateScreen
import fr.uha.hassenforder.android.ui.SwipeableItem
import fr.uha.hassenforder.android.ui.app.AppTopBar
import fr.uha.hassenforder.android.ui.app.UITitleState
import fr.uha.hassenforder.team.R
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.generated.destinations.CreateShiftScreenDestination
import fr.uha.hassenforder.android.icons.TimeStart
import fr.uha.hassenforder.team.model.Shift
import com.ramcosta.composedestinations.generated.destinations.EditShiftScreenDestination
import fr.uha.hassenforder.team.ui.shift.ListShiftsViewModel

@Destination<RootGraph>
@Composable
fun ListShiftsScreen (
    vm : ListShiftsViewModel = hiltViewModel(),
    navigator : DestinationsNavigator
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    Scaffold (
        topBar = { AppTopBar(UITitleState(screenNameId = R.string.list_shift)) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigator.navigate(CreateShiftScreenDestination) },
            ) {
                Icon(Icons.Filled.Add, contentDescription = "add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            StateScreen(state = uiState) { content ->
                SuccessListShiftsScreen(content, navigator) { vm.send(it) }
            }
        }
    }
}

@Composable
fun SuccessListShiftsScreen (
    uiState: ListShiftsViewModel.UIState,
    navigator : DestinationsNavigator,
    send : (ListShiftsViewModel.UIEvent) -> Unit
) {
    LazyColumn () {
        items(
            items = uiState.shifts,
            key = { item -> item.sid }
        ) { item ->
            SwipeableItem (
                onEdit = { navigator.navigate(EditShiftScreenDestination(item.sid)) },
                onDelete = { send(ListShiftsViewModel.UIEvent.OnDelete(item)) }
            ) {
                ShiftItem (item)
            }
        }
    }
}

@Composable
fun ShiftItem (shift: Shift) {
    ListItem (
        headlineContent = {
            Row (horizontalArrangement = Arrangement.spacedBy(6.dp)){
                Text(shift.location)
            }
        },
        supportingContent = {
            Row (
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.TimeStart, contentDescription = "phone")
                Text("Dp : ")
                Text(shift.startDate, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(",")
                Text(" Ar : ")
                Text(shift.endDate, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Icon(imageVector = Icons.TimeStart, contentDescription = "phone")
            }
        },
    )
}