package fr.uha.hassenforder.team.ui.shift

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BuildCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import fr.uha.hassenforder.team.model.Vehicle
import fr.uha.hassenforder.team.model.VehicleStatus

@Composable
fun ShiftVehicle (
    vehicle : Vehicle,
    modifier: Modifier = Modifier,
) {
    val status : ImageVector =
        when(vehicle.status) {
            VehicleStatus.IN_USE -> Icons.Outlined.PlayCircle
            VehicleStatus.MAINTENANCE -> Icons.Outlined.BuildCircle
            VehicleStatus.AVAILABLE -> Icons.Outlined.CheckCircle
            VehicleStatus.UNAVAILABLE -> Icons.Outlined.RemoveCircleOutline
        }
    ListItem (
        headlineContent = {
            Row (
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(vehicle.brandAndModel)
                Text(vehicle.matriculation)
                Text(vehicle.kilometers.toString())
            }
        },
        trailingContent = {
            Icon(imageVector = status, contentDescription = "status", modifier = Modifier.size(48.dp))
        }
    )
}