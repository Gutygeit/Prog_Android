package fr.uha.hassenforder.team.ui.person

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import fr.uha.hassenforder.android.ui.field.Orientation
import fr.uha.hassenforder.android.ui.field.OutlinedEnumRadioGroupWrapper
import fr.uha.hassenforder.android.ui.field.OutlinedPictureFieldWrapper
import fr.uha.hassenforder.android.ui.field.OutlinedTextFieldWrapper
import fr.uha.hassenforder.android.ui.field.PictureFieldConfig
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.TeamFileProvider
import fr.uha.hassenforder.team.model.Gender

@Composable
fun SuccessPersonScreen(
    person: PersonViewModel.UIState,
    send : (PersonViewModel.UIEvent) -> Unit,
) {
    val context = LocalContext.current

    Column {
        OutlinedTextFieldWrapper(
            field = person.firstnameState,
            onValueChange = { send(PersonViewModel.UIEvent.FirstnameChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            labelId = R.string.firstname,
        )
        OutlinedTextFieldWrapper(
            field = person.lastnameState,
            onValueChange = { send(PersonViewModel.UIEvent.LastnameChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            labelId = R.string.lastname,
        )
        OutlinedTextFieldWrapper(
            field = person.phoneState,
            onValueChange = { send(PersonViewModel.UIEvent.PhoneChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            labelId = R.string.phone,
        )
        OutlinedEnumRadioGroupWrapper(
            field = person.genderState,
            onValueChange = { send(PersonViewModel.UIEvent.GenderChanged(Gender.valueOf(it))) },
            modifier = Modifier.fillMaxWidth(),
            orientation = Orientation.HORIZONTAL,
            itemValues = Gender.values(),
            labelId = R.string.gender,
        )
        OutlinedPictureFieldWrapper(
            field = person.pictureState,
            onValueChange = { send(PersonViewModel.UIEvent.PictureChanged(it)) },
            config = PictureFieldConfig(
                galleryFilter = "image/*",
                newImageUriProvider = { TeamFileProvider.getImageUri(context) },
            ),
            modifier = Modifier.fillMaxWidth(),
            labelId = R.string.picture,
        )
    }
}
