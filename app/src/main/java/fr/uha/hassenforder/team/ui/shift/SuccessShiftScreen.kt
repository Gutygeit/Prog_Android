package fr.uha.hassenforder.team.ui.shift

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.uha.hassenforder.android.ui.field.OutlinedTextFieldWrapper
import fr.uha.hassenforder.team.R

@Composable
fun SuccessShiftScreen(
    shift: ShiftViewModel.UIState,
    send : (ShiftViewModel.UIEvent) -> Unit,
) {
    Scaffold (
        topBar = {
        }
    ){ innerPadding ->
        Column (
            modifier = Modifier.padding(innerPadding)
        ) {
            OutlinedTextFieldWrapper(
                field = shift.location,
                onValueChange = { send(ShiftViewModel.UIEvent.LocationChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                labelId = R.string.location,
            )
            OutlinedTextFieldWrapper(
                field = shift.startDate,
                onValueChange = { send(ShiftViewModel.UIEvent.StartDateChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                labelId = R.string.start_date,
            )
            OutlinedTextFieldWrapper(
                field = shift.endDate,
                onValueChange = { send(ShiftViewModel.UIEvent.EndDateChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                labelId = R.string.end_date,
            )
            OutlinedVehicleFieldWrapper(
                field = shift.vehicle,
                onValueChange = { send(ShiftViewModel.UIEvent.VehicleChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                labelId = R.string.vehicle,
            )
            OutlinedDriverFieldWrapper(
                field = shift.driver,
                onValueChange = { send(ShiftViewModel.UIEvent.DriverChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                labelId = R.string.driver,
            )
            OutlinedPassengersFieldWrapper(
                field = shift.passengers,
                onAddMember = { send(ShiftViewModel.UIEvent.AddMember(it)) },
                onRemoveMember = { send(ShiftViewModel.UIEvent.RemoveMember(it)) },
                modifier = Modifier.fillMaxWidth(),
                labelId = R.string.passengers,
            )
        }
    }
}