package app.test.demochat.presentation.screens.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.test.demochat.data.model.UserParams
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    userParams: UserParams,
) {
    val viewModel = koinViewModel<RegistrationViewModel> { parametersOf(userParams) }
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
                title = { Text("Регистрация") },
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    viewModel.register()
                },
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                enabled = state.name.isNotBlank() && state.username.isNotBlank()
            ) {
                Text("Сохранить")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Номер телефона: ${state.userParams.phoneNumber}",
                style = MaterialTheme.typography.bodyLarge
            )

            TextField(
                value = state.name,
                onValueChange = { viewModel.setName(it) },
                label = { Text("Имя") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = state.username,
                onValueChange = {
                    if (it.matches(Regex("^[A-Za-z0-9_-]*$"))) {
                        viewModel.setUsername(it)
                    }
                },
                label = { Text("Логин") },
                modifier = Modifier.fillMaxWidth()
            )

            if (state.showProgressBar) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}