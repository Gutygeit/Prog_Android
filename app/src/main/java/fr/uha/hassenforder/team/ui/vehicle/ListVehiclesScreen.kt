package fr.uha.hassenforder.team.ui.vehicle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.BuildCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.CreateVehicleScreenDestinationDestination
import com.ramcosta.composedestinations.generated.destinations.EditDriverScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.hassenforder.android.ui.StateScreen
import fr.uha.hassenforder.android.ui.SwipeableItem
import fr.uha.hassenforder.android.ui.app.AppTopBar
import fr.uha.hassenforder.android.ui.app.UITitleState
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Vehicle
import fr.uha.hassenforder.team.model.VehicleStatus

@Destination<RootGraph>
@Composable
fun ListVehiclesScreen (
    vm : ListVehiclesViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { AppTopBar(UITitleState(screenNameId = R.string.list_vehicle)) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigator.navigate(CreateVehicleScreenDestinationDestination) },
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column (
            modifier = Modifier.padding(innerPadding)
        ){
            StateScreen(state = uiState) { content ->
                SuccessListVehiclesScreen(content, navigator) { vm.send(it) }
            }
        }
    }
}

@Composable
fun SuccessListVehiclesScreen (
    uiState: ListVehiclesViewModel.UIState,
    navigator : DestinationsNavigator,
    send : (ListVehiclesViewModel.UIEvent) -> Unit
) {
    LazyColumn {
        items(
            items = uiState.vehicles,
            key = { item -> item.vid }
        ) { item ->
            SwipeableItem (
                onEdit = { navigator.navigate(EditDriverScreenDestination(item.vid)) },
                onDelete = { send(ListVehiclesViewModel.UIEvent.OnDelete(item)) }
            ) {
                VehicleItem (item)
            }
        }
    }
}

@Composable
fun VehicleItem(vehicle: Vehicle) {
    val status : ImageVector =
        when(vehicle.status) {
            VehicleStatus.AVAILABLE -> Icons.Outlined.CheckCircle
            VehicleStatus.IN_USE -> Icons.Outlined.PlayCircle
            VehicleStatus.MAINTENANCE -> Icons.Outlined.BuildCircle
            VehicleStatus.UNAVAILABLE -> Icons.Outlined.RemoveCircleOutline
        }
    ListItem (
        headlineContent = {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(vehicle.brandAndModel)
                Text(vehicle.matriculation)
                Text(vehicle.kilometers.toString())
            }
        },
        trailingContent = {
            Icon(imageVector = status, contentDescription = "license", modifier = Modifier.size(48.dp))
        }
    )
}



