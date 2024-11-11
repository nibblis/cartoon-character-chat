package app.test.demochat.presentation.screens.profile.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.test.demochat.presentation.screens.profile.components.DatePickerField
import app.test.demochat.presentation.screens.profile.components.ImagePicker
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.messageState) {
        if (state.messageState != null) {
            snackbarHostState.showSnackbar(state.messageState!!)
        }
    }

    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Профиль") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onBackPressed() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = { viewModel.updateProfile() },
                modifier = Modifier.fillMaxWidth().padding(32.dp)
            ) {
                Text("Сохранить")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            ImagePicker(
                currentAvatar = state.avatar,
                onImageSelected = { filename, base64 ->
                    viewModel.setAvatar(filename, base64)
                }
            )

            Text(
                text = state.profile?.name.orEmpty(),
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Логин: ${state.profile?.username}",
                style = MaterialTheme.typography.bodyLarge
            )

            TextField(
                value = state.profile?.name.orEmpty(),
                onValueChange = { viewModel.setName(it) },
                label = { Text("Имя") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            TextField(
                value = state.profile?.city.orEmpty(),
                onValueChange = { viewModel.setСity(it) },
                label = { Text("Город") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            DatePickerField(
                value = state.profile?.birthday.orEmpty(),
                onValueChange = { viewModel.setBirthday(it) },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = state.profile?.about.orEmpty(),
                onValueChange = { viewModel.setAbout(it) },
                label = { Text("О себе") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )
        }
    }
}