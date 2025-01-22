package fr.uha.hassenforder.team.ui.shift

import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import fr.uha.hassenforder.team.model.Driver
import fr.uha.hassenforder.team.model.License
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.DoNotDisturb
import androidx.compose.material.icons.outlined.Female
import androidx.compose.material.icons.outlined.FireTruck
import androidx.compose.material.icons.outlined.Male
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ShiftDriver (
    driver : Driver,
    modifier: Modifier = Modifier,
) {
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

