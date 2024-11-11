package app.test.demochat.presentation.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import app.test.demochat.utils.asImageBitmap
import app.test.demochat.utils.formatDate
import app.test.demochat.utils.formatPhoneNumber
import app.test.demochat.utils.getZodiacSign
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

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
                },
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Profile"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = { viewModel.navigateToEditProfile() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            ) {
                Text("Редактировать профиль")
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (state.avatar != null) {
                Image(
                    bitmap = state.avatar!!.asImageBitmap(),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
            }

            Text(
                text = state.profile?.name.orEmpty(),
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Логин: ${state.profile?.username}",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Телефон: ${state.profile?.phone.orEmpty().formatPhoneNumber()}",
                style = MaterialTheme.typography.bodyLarge
            )

            if (!state.profile?.city.isNullOrBlank()) {
                Text(
                    text = "Город: ${state.profile?.city}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            if (!state.profile?.birthday.isNullOrBlank()) {
                val formattedDate = formatDate(state.profile?.birthday.orEmpty())

                Text(
                    text = "Дата рождения: $formattedDate",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            if (!state.profile?.birthday.isNullOrBlank()) {
                val zodiacSign = getZodiacSign(state.profile?.birthday.orEmpty())

                Text(
                    text = "Знак зодиака: $zodiacSign",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            if (!state.profile?.about.isNullOrBlank()) {
                Text(
                    text = "О себе: ${state.profile?.about}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        if (showLogoutDialog) {
            ConfirmationDialog(
                onConfirm = {
                    showLogoutDialog = false
                    viewModel.logout()
                },
                onDismiss = { showLogoutDialog = false }
            )
        }
    }
}

@Composable
private fun ConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                style = MaterialTheme.typography.titleMedium,
                text = "Вы действительно хотите выйти из аккаунта?"
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Выйти")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}