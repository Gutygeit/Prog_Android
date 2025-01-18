package fr.uha.hassenforder.team.ui.team

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.uha.hassenforder.android.ui.field.FieldWrapper
import fr.uha.hassenforder.android.ui.field.OutlinedDecorator
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Person

@Composable
fun LeaderField (
    value: Person?,
    onValueChange : (Long?) -> Unit,
    modifier : Modifier = Modifier,
    @StringRes labelId: Int? = null,
    @StringRes errorId: Int?,
) {
    val showDialog =  remember { mutableStateOf(false) }

    if (showDialog.value) {
        PersonPicker(
            title = R.string.leader_select,
            onSelect = { showDialog.value = false; if (it != null) onValueChange(it.pid) }
        )
    }
    OutlinedDecorator (
        modifier = modifier
            .clickable(
                onClick = { showDialog.value = true; }
            ),
        labelId = labelId,
        errorId = errorId,
    ) {
        Row {
            if (value != null) {
                TeamPersonItem (value, modifier = Modifier.weight(1.0F))
            } else {
                Column (modifier = Modifier.weight(1.0F)) { }
            }
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "remove",
                modifier = Modifier.clickable(
                    onClick = { onValueChange(null) }
                )
            )
        }
    }
}

@Composable
fun LeaderFieldWrapper (
    field: FieldWrapper<Person>,
    onValueChange : (Long?) -> Unit,
    modifier : Modifier = Modifier,
    @StringRes labelId: Int? = null,
) {
    LeaderField (
        value = field.value,
        onValueChange = onValueChange,
        modifier = modifier,
        labelId = labelId,
        errorId = field.errorId,
    )
}