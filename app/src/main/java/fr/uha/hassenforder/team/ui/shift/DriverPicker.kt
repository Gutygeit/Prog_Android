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
import fr.uha.hassenforder.team.database.DriverDao
import fr.uha.hassenforder.team.model.Driver
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class DriverPickerViewModel @Inject constructor (private val dao: DriverDao): ViewModel() {
    val drivers: Flow<List<Driver>> = dao.getAll()
}

@Composable
fun DriverPicker(
    vm : DriverPickerViewModel = hiltViewModel(),
    titleId: Int,
    onSelect: (Driver?) -> Unit,
) {
    val drivers = vm.drivers.collectAsStateWithLifecycle(initialValue = emptyList())
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
                    items = drivers.value,
                    key = { driver -> driver.did }
                ) {
                        item ->
                    Box (
                        modifier = Modifier.clickable(
                            onClick = { onSelect(item) }
                        )
                    ) {
                        ShiftDriver(item)
                    }
                }
            }
        }
    }
}