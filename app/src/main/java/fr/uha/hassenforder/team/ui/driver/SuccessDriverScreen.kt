package fr.uha.hassenforder.team.ui.driver

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import fr.uha.hassenforder.android.ui.field.Orientation
import fr.uha.hassenforder.android.ui.field.OutlinedEnumRadioGroupWrapper
import fr.uha.hassenforder.android.ui.field.OutlinedTextFieldWrapper
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.License

@Composable
fun SuccessDriverScreen(
    driver: DriverViewModel.UIState,
    send : (DriverViewModel.UIEvent) -> Unit,
) {
    val context = LocalContext.current

    Column()
    {
        OutlinedTextFieldWrapper(
            field = driver.firstnameState,
            onValueChange = { send(DriverViewModel.UIEvent.FirstnameChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            labelId = R.string.firstname,
        )
        OutlinedTextFieldWrapper(
            field = driver.lastnameState,
            onValueChange = { send(DriverViewModel.UIEvent.LastnameChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            labelId = R.string.lastname,
        )
        OutlinedTextFieldWrapper(
            field = driver.phoneState,
            onValueChange = { send(DriverViewModel.UIEvent.PhoneChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            labelId = R.string.phone,
        )
        OutlinedEnumRadioGroupWrapper(
            field = driver.licenseState,
            onValueChange = { send(DriverViewModel.UIEvent.GenderChanged(License.valueOf(it))) },
            modifier = Modifier.fillMaxWidth(),
            orientation = Orientation.HORIZONTAL,
            itemValues = License.values(),
            labelId = R.string.license,
        )
    }
}