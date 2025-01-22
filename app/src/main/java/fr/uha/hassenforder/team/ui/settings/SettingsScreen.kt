package fr.uha.hassenforder.team.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.FireTruck
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.hassenforder.android.ui.SwipeableItem
import fr.uha.hassenforder.android.ui.app.AppTopBar
import fr.uha.hassenforder.android.ui.app.UITitleState
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Driver
import fr.uha.hassenforder.team.model.License
import fr.uha.hassenforder.team.ui.driver.ListDriversViewModel

@Destination<RootGraph>
@Composable
fun SettingsScreen (
    vm : SettingsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    Scaffold (
        topBar = { AppTopBar(UITitleState(screenNameId = R.string.settings)) },
    ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                Button(
                    onClick = { vm.onClear() }
                ) {
                    Text("Nettoyer")
                }
                Button(
                    onClick = { vm.onFill() }
                ) {
                    Text("Remplir")
                }
            }
        }
}

@Composable
fun SuccessListDriversScreen (
    uiState: ListDriversViewModel.UIState,
    navigator : DestinationsNavigator,
    send : (ListDriversViewModel.UIEvent) -> Unit
) {
    LazyColumn () {
        items(
            items = uiState.drivers,
            key = { item -> item.did }
        ) { item ->
            SwipeableItem (
                onEdit = { navigator.navigate(
                    com.ramcosta.composedestinations.generated.destinations.EditDriverScreenDestination(
                        item.did
                    )
                ) },
                onDelete = { send(ListDriversViewModel.UIEvent.OnDelete(item)) }
            ) {
                DriverItem (item)
            }
        }
    }
}

@Composable
fun DriverItem (driver : Driver) {
    val license : ImageVector =
        when(driver.license) {
            License.A -> Icons.Outlined.DirectionsCar
            License.B -> Icons.Outlined.DirectionsBus
            License.C -> Icons.Outlined.FireTruck
        }
    ListItem (
        headlineContent = {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(driver.firstname)
                Text(driver.lastname)
            }
        },
        supportingContent = {
            Row (
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Outlined.Phone, contentDescription = "phone")
                Text(driver.phone, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        trailingContent = {
            Icon(imageVector = license, contentDescription = "license", modifier = Modifier.size(48.dp))
        }
    )
}