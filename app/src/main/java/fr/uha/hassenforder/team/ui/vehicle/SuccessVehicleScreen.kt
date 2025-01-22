package fr.uha.hassenforder.team.ui.vehicle

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import fr.uha.hassenforder.android.ui.field.Orientation
import fr.uha.hassenforder.android.ui.field.OutlinedEnumRadioGroupWrapper
import fr.uha.hassenforder.android.ui.field.OutlinedIntFieldWrapper
import fr.uha.hassenforder.android.ui.field.OutlinedTextFieldWrapper
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.VehicleStatus

@Composable
fun SuccessVehicleScreen(
    vehicle: VehicleViewModel.UIState,
    send : (VehicleViewModel.UIEvent) -> Unit,
) {
    val context = LocalContext.current

    Column {
        OutlinedTextFieldWrapper(
            field = vehicle.brandAndModelState,
            onValueChange = { send(VehicleViewModel.UIEvent.BrandAndModelChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            labelId = R.string.brand_and_model,
        )
        OutlinedTextFieldWrapper(
            field = vehicle.matriculationState,
            onValueChange = { send(VehicleViewModel.UIEvent.MatriculationChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            labelId = R.string.matriculation,
        )
        OutlinedIntFieldWrapper(
            field = vehicle.kilometersState,
            onValueChange = { send(VehicleViewModel.UIEvent.KilometersChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            labelId = R.string.kilometers,
            )
        OutlinedEnumRadioGroupWrapper(
            field = vehicle.vehicleStatusState,
            onValueChange = { send(VehicleViewModel.UIEvent.VehicleStatusChanged(VehicleStatus.valueOf(it)))},
            modifier = Modifier.fillMaxWidth(),
            orientation = Orientation.HORIZONTAL,
            itemValues = VehicleStatus.values(),
            labelId = R.string.status,
        )
    }
}
