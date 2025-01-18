package fr.uha.hassenforder.team.ui.person

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
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Leaderboard
import androidx.compose.material.icons.outlined.Male
import androidx.compose.material.icons.outlined.Phone
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.CreatePersonScreenDestination
import com.ramcosta.composedestinations.generated.destinations.EditPersonScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import fr.uha.hassenforder.android.ui.StateScreen
import fr.uha.hassenforder.android.ui.SwipeableItem
import fr.uha.hassenforder.android.ui.app.AppTopBar
import fr.uha.hassenforder.android.ui.app.UITitleState
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Gender
import fr.uha.hassenforder.team.model.Person
import fr.uha.hassenforder.team.model.PersonWithDetails

@Destination<RootGraph>
@Composable
fun ListPersonsScreen(
    vm: ListPersonsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { AppTopBar( UITitleState(screenNameId = R.string.title_person_list) ) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navigator.navigate(CreatePersonScreenDestination) } ) {
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
    fields: ListPersonsViewModel.UIState,
    send : (ListPersonsViewModel.UIEvent) -> Unit,
    navigator: DestinationsNavigator,
) {
    LazyColumn {
        items(
            items = fields.persons,
            key = { person -> person.person.pid }
        ) { item ->
            SwipeableItem(
                onEdit = { navigator.navigate(EditPersonScreenDestination (pid = item.person.pid)) },
                onDelete = { send (ListPersonsViewModel.UIEvent.OnDelete(item.person)) }
            ) { PersonItem(item) }
        }
    }
}

@Composable
private fun PersonItem (person : PersonWithDetails) {
    val gender : ImageVector =
            when (person.person.gender) {
                Gender.NO -> Icons.Outlined.DoNotDisturb
                Gender.GIRL -> Icons.Outlined.Female
                Gender.BOY -> Icons.Outlined.Male
            }
    ListItem (
        headlineContent = {
            Row {
                Text(person.person.firstname, modifier = Modifier.padding(end = 8.dp))
                Text(person.person.lastname)
            }
        },
        supportingContent = {
            Column {
                Row {
                    Icon(
                        imageVector = Icons.Outlined.Phone,
                        contentDescription = "phone",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(person.person.phone, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Row {
                    Icon(
                        imageVector = Icons.Outlined.Leaderboard,
                        contentDescription = "leader",
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                    )
                    Text(person.leadingCount.toString())
                    Icon(
                        imageVector = Icons.Outlined.Group,
                        contentDescription = "group",
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                    )
                    Text(person.membersCount.toString())
                }
            }
        },
        leadingContent = {
            if (person.person.picture != null) {
                AsyncImage(
                    model = person.person.picture,
                    modifier = Modifier.size(64.dp),
                    contentDescription = null,
                    error = rememberVectorPainter(Icons.Outlined.Error),
                    placeholder = rememberVectorPainter(Icons.Outlined.Casino),
                )
            }
        },
        trailingContent = {
            Icon(imageVector = gender, contentDescription = "gender", modifier = Modifier.size(48.dp) )
        },
    )
}
