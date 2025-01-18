package fr.uha.hassenforder.team.ui.team

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import fr.uha.hassenforder.android.ui.SwipeableItem
import fr.uha.hassenforder.android.ui.field.FieldWrapper
import fr.uha.hassenforder.android.ui.field.OutlinedDecorator
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Person

@Composable
fun ListMembersField(
    value : List<Person>,
    onAdd: (pid : Long) -> Unit,
    onDelete: (person : Person) -> Unit,
    modifier : Modifier = Modifier,
    @StringRes labelId: Int? = null,
    @StringRes errorId : Int?,
) {
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        PersonPicker(
            title = R.string.member_select,
            onSelect = { showDialog.value = false; if (it != null) onAdd(it.pid) }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog.value = true }) {
                Icon(Icons.Filled.Add, contentDescription = "add")
            }
        }
    ) { innerPadding ->
        OutlinedDecorator(
            modifier = Modifier.padding(innerPadding),
            labelId = labelId,
            errorId = errorId,
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(
                    items = value,
                    key = { person: Person -> person.pid }
                ) { item: Person ->
                    HorizontalDivider(color = MaterialTheme.colorScheme.onBackground)
                    SwipeableItem(
                        onDelete = { onDelete(item) }
                    ) {
                        TeamPersonItem(item)
                    }
                }
            }
        }
    }
}

@Composable
fun ListMembersFieldWrapper(
    field : FieldWrapper<List<Person>>,
    onAdd: (pid : Long) -> Unit,
    onDelete: (person : Person) -> Unit,
    modifier : Modifier = Modifier,
    @StringRes labelId: Int? = null,
) {
    ListMembersField (
        value = field.value ?: emptyList(),
        onAdd = onAdd,
        onDelete = onDelete,
        modifier = modifier,
        labelId = labelId,
        errorId = field.errorId,
    )
}