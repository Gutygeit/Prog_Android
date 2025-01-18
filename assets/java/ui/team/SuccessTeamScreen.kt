package fr.uha.hassenforder.team.ui.team

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import fr.uha.hassenforder.android.ui.Converter
import fr.uha.hassenforder.android.ui.field.OutlinedDateFieldWrapper
import fr.uha.hassenforder.android.ui.field.OutlinedIntFieldWrapper
import fr.uha.hassenforder.android.ui.field.OutlinedIntSpinnerFieldWrapper
import fr.uha.hassenforder.android.ui.field.OutlinedTextField
import fr.uha.hassenforder.android.ui.field.OutlinedTextFieldWrapper
import fr.uha.hassenforder.team.R

@Composable
fun SuccessTeamScreen(
    team: TeamViewModel.UIState,
    send : (TeamViewModel.UIEvent) -> Unit,
) {
    Column {
        OutlinedTextFieldWrapper(
            field = team.name,
            onValueChange = { send(TeamViewModel.UIEvent.NameChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            labelId = R.string.teamname,
        )
        OutlinedDateFieldWrapper(
            field = team.startDay,
            onValueChange = { send(TeamViewModel.UIEvent.StartDayChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            labelId = R.string.start_day
        )
        OutlinedIntSpinnerFieldWrapper (
            field = team.duration,
            onValueChange = { send(TeamViewModel.UIEvent.DurationChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            labelId = R.string.duration,
            optionLabels = stringArrayResource(id = R.array.durations),
            optionValues = stringArrayResource(id = R.array.durations),
        )
        LeaderFieldWrapper(
            field = team.leader,
            onValueChange = { send(TeamViewModel.UIEvent.LeaderChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            labelId = R.string.leader
        )
        ListMembersFieldWrapper(
            field = team.members,
            onAdd = { send(TeamViewModel.UIEvent.MemberAdded(it)) },
            onDelete = { send(TeamViewModel.UIEvent.MemberDeleted(it)) },
            modifier = Modifier.fillMaxWidth(),
            labelId = R.string.members
        )
    }
}
