package fr.uha.hassenforder.team.ui.shift

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import fr.uha.hassenforder.android.ui.field.FieldWrapper
import fr.uha.hassenforder.android.ui.field.OutlinedDecorator
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Vehicle

@Composable
fun OutlineVehicleField(
    value: Vehicle?,
    onValueChange: (Vehicle?) -> Unit,
    modifier: Modifier,
    labelId: Int?,
    errorId: Int?
){
    val showDialog =  remember { mutableStateOf(false) }

    if (showDialog.value) {
        VehiclePicker (
            titleId = R.string.vehicle_picker,
        ) { showDialog.value = false; if (it != null) onValueChange(it) }
    }

    OutlinedDecorator (
        modifier = modifier,
        labelId = labelId,
        errorId = errorId,
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = { showDialog.value = true }
                )
        ){
            if (value != null)
                ShiftVehicle(value, modifier = Modifier.weight(1.0F))
            IconButton(
                onClick = { onValueChange(null) }
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "delete")
            }
        }
    }
}

@Composable
fun OutlinedVehicleFieldWrapper (
    field : FieldWrapper<Vehicle?>,
    onValueChange: (Vehicle?) -> Unit,
    modifier : Modifier = Modifier,
    @StringRes labelId: Int? = null,
) {
    OutlineVehicleField(
        value = field.value,
        onValueChange = onValueChange,
        modifier = modifier,
        labelId = labelId,
        errorId = field.errorId,
    )
}