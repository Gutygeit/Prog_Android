package fr.uha.hassenforder.team.ui.shift

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.android.ui.app.AppTitle
import fr.uha.hassenforder.team.database.VehicleDao
import fr.uha.hassenforder.team.model.Vehicle
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class VehiclePickerViewModel @Inject constructor (private val dao: VehicleDao): ViewModel() {
    val vehicles: Flow<List<Vehicle>> = dao.getAll()
}

@Composable
fun VehiclePicker(
    vm : VehiclePickerViewModel = hiltViewModel(),
    titleId: Int,
    onSelect: (Vehicle?) -> Unit,
) {
    val vehicles = vm.vehicles.collectAsStateWithLifecycle(initialValue = emptyList())
    Dialog(onDismissRequest = { onSelect(null) }) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { AppTitle(screenTitleId = titleId) },
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ) {
                items(
                    items = vehicles.value,
                    key = { vehicle -> vehicle.vid }
                ) {
                        item ->
                    Box (
                        modifier = Modifier.clickable(
                            onClick = { onSelect(item) }
                        )
                    ) {
                        ShiftVehicle(item)
                    }
                }
            }
        }
    }
}