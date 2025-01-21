package fr.uha.hassenforder.team.ui.shift

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.uha.hassenforder.android.ui.field.OutlinedIntField
import fr.uha.hassenforder.android.ui.field.OutlinedIntFieldWrapper
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
                field = shift.date,
                onValueChange = { send(ShiftViewModel.UIEvent.DateChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                labelId = R.string.date,
            )
            OutlinedIntFieldWrapper(
                field = shift.duration,
                onValueChange = { send(ShiftViewModel.UIEvent.DurationChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                labelId = R.string.duration,
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