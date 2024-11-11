package app.test.demochat.presentation.screens.auth.verify_code

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import app.test.demochat.data.model.AuthParams
import app.test.demochat.presentation.screens.auth.components.OtpInputField
import app.test.demochat.utils.formatByMask
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyCodeScreen(
    authParams: AuthParams,
) {
    val viewModel = koinViewModel<VerifyCodeViewModel> { parametersOf(authParams) }

    val state by viewModel.state.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.messageState) {
        if (state.messageState != null) {
            snackbarHostState.showSnackbar(state.messageState!!)
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { viewModel.onBackPressed() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(120.dp))
                Text(
                    modifier = Modifier.padding(horizontal = 30.dp),
                    style = MaterialTheme.typography.titleLarge,
                    text = "Введите код отправленный на номер телефона \n${state.authParams.dialCode} ${
                        formatByMask(
                            mask = state.authParams.mask,
                            input = state.authParams.phoneNumber
                        )
                    }"
                )

                OtpInputField(
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .focusRequester(focusRequester),
                    otpText = state.otpCode,
                    shouldShowError = state.shouldShowCodeError,
                    onOtpModified = { value, otpFilled ->
                        viewModel.onOtpModified(value)
                        if (otpFilled) {
                            keyboardController?.hide()
                            viewModel.onOtpEntered()
                        }
                    }
                )
                if (state.shouldShowCodeError) {
                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = "Неверный код",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if (state.showProgressBar) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
private fun ConfirmationDialog(
    dialCode: String,
    phoneNumber: String,
    currentMask: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                style = MaterialTheme.typography.titleMedium,
                text = "Отправить код подтверждения на номер $dialCode ${
                    formatByMask(
                        currentMask,
                        phoneNumber
                    )
                }?"
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Отправить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

@Composable
private fun ErrorDialog(
    errorMessage: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ошибка") },
        text = { Text(errorMessage) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}