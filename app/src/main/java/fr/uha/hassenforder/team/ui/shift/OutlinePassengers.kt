package fr.uha.hassenforder.team.ui.shift

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Scaffold
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import fr.uha.hassenforder.android.ui.field.FieldWrapper
import fr.uha.hassenforder.android.ui.field.OutlinedDecorator
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Driver

@Composable
fun OutlinedPassengersField(
    value: List<Driver>,
    onAddMember: (Driver) -> Unit,
    onRemoveMember: (Driver) -> Unit,
    modifier: Modifier,
    labelId: Int?,
    errorId: Int?
) {
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        DriverPicker (
            titleId = R.string.driver_picker,
        ) { showDialog.value = false; if (it != null) onAddMember(it) }
    }

    OutlinedDecorator (
        modifier = modifier,
        labelId = labelId,
        errorId = errorId,
    ){
        Scaffold (
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showDialog.value = true },
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "add")
                }
            }
        )
        { innerPadding ->
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(
                    items = value,
                    key = { driver -> driver.did }
                ) {
                        item ->
                    Box (
                        modifier = Modifier.clickable(
                            onClick = { onRemoveMember(item) }
                        )
                    ) {
                        ShiftDriver(item)
                    }
                }
            }
        }
    }
}

@Composable
fun OutlinedPassengersFieldWrapper (
    field : FieldWrapper<List<Driver>>,
    onAddMember: (Driver) -> Unit,
    onRemoveMember: (Driver) -> Unit,
    modifier : Modifier = Modifier,
    @StringRes labelId : Int? = null,
) {
    OutlinedPassengersField(
        value = field.value ?: emptyList(),
        onAddMember = onAddMember,
        onRemoveMember = onRemoveMember,
        modifier = modifier,
        labelId = labelId,
        errorId = field.errorId,
    )
}