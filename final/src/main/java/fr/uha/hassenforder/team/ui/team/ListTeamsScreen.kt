package fr.uha.hassenforder.team.ui.team

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.DoNotDisturb
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Female
import androidx.compose.material.icons.outlined.Male
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Start
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.CreateTeamScreenDestination
import com.ramcosta.composedestinations.generated.destinations.EditTeamScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.hassenforder.android.ui.Converter
import fr.uha.hassenforder.android.ui.StateScreen
import fr.uha.hassenforder.android.ui.SwipeableItem
import fr.uha.hassenforder.android.ui.app.AppTopBar
import fr.uha.hassenforder.android.ui.app.UITitleState
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Team

@Destination<RootGraph>
@Composable
fun ListTeamsScreen(
    vm: ListTeamsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { AppTopBar( UITitleState(screenNameId = R.string.title_team_list) ) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navigator.navigate(CreateTeamScreenDestination) } ) {
                Icon(Icons.Filled.Add, contentDescription = "add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            StateScreen(uiState) {
                SuccessListScreen(it, { vm.send(it) }, navigator)
            }
        }
    }
}

@Composable
private fun SuccessListScreen(
    fields: ListTeamsViewModel.UIState,
    send : (ListTeamsViewModel.UIEvent) -> Unit,
    navigator: DestinationsNavigator,
) {
    LazyColumn {
        items(
            items = fields.teams,
            key = { team -> team.tid }
        ) { item ->
            SwipeableItem(
                onEdit = { navigator.navigate(EditTeamScreenDestination (tid = item.tid)) },
                onDelete = { send (ListTeamsViewModel.UIEvent.OnDelete(item)) }
            ) { TeamItem(item) }
        }
    }
}

@Composable
private fun TeamItem (team : Team) {
    ListItem (
        headlineContent = {
            Text(team.name)
        },
        supportingContent = {
            Row (horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(
                    imageVector = Icons.Outlined.Start,
                    contentDescription = "start",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(Converter.convert(team.startDay), fontWeight = FontWeight.Bold)
                Text(stringResource(id = R.string.duration))
                Text(team.duration.toString(), fontWeight = FontWeight.Bold)
            }
        },
    )
}
