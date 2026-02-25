package app.test.demochat.presentation.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import app.test.demochat.presentation.screens.auth.components.PhoneNumberTransformation
import app.test.demochat.presentation.screens.auth.components.CountryPickerDialog
import app.test.demochat.utils.formatByMask
import org.koin.androidx.compose.koinViewModel

private const val COUNTRY_CODE_PREFIX = "+"
private const val COUNTRY_CODE_MAX_LENGTH = 5

/**
 * Отображает главный экран аутентификации.
 *
 * Этот экран позволяет пользователю вводить номер телефона, выбирать код страны и инициировать процесс входа.
 * Он также обрабатывает состояния загрузки и ошибок, поступающие от [viewModel].
 *
 * @param viewModel ViewModel, управляющая логикой экрана.
 */
@Composable
fun AuthScreen(
    viewModel: AuthViewModel = koinViewModel(),
) {

    val state by viewModel.state.collectAsState()

    var showConfirmDialog by remember { mutableStateOf(false) }
    var showCountryCodePicker by remember { mutableStateOf(false) }

    var dialCodeValue by remember { mutableStateOf(TextFieldValue(state.selectedCountry?.code.orEmpty())) }
    
    // РЕФАКТОРИНГ: Применен Early Return для чистоты и безопасности
    LaunchedEffect(state.selectedCountry) {
        val country = state.selectedCountry ?: return@LaunchedEffect

        dialCodeValue = TextFieldValue(
            text = country.code,
            selection = TextRange(country.code.length)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(bottom = 60.dp),
                style = MaterialTheme.typography.titleLarge,
                text = "Введите номер телефона"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .padding(horizontal = 32.dp)
                    .border(
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    modifier = Modifier
                        .weight(0.3f)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = state.selectedCountry?.flag ?: "❓",
                            modifier = Modifier
                                .padding(end = 6.dp)
                                .clickable { showCountryCodePicker = true }
                        )
                        BasicTextField(
                            value = dialCodeValue,
                            onValueChange = { newValue ->
                                val filteredText =
                                    newValue.text.filter { it.isDigit() || it == COUNTRY_CODE_PREFIX.first() }

                                if (filteredText.length > COUNTRY_CODE_MAX_LENGTH) return@BasicTextField

                                val updatedText = if (!filteredText.startsWith(COUNTRY_CODE_PREFIX)) {
                                    "$COUNTRY_CODE_PREFIX$filteredText"
                                } else {
                                    filteredText
                                }

                                dialCodeValue = TextFieldValue(
                                    text = updatedText,
                                    selection = TextRange(updatedText.length)
                                )

                                val selectedCountry = state.countries.find {
                                    updatedText.startsWith(it.code) && updatedText.length == it.code.length
                                }
                                viewModel.setDialCode(dialCodeValue.text)
                                viewModel.setSelectedCountry(selectedCountry)
                            },
                            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.width(IntrinsicSize.Min)
                        )
                    }
                    Icon(
                        modifier = Modifier
                            .clickable { showCountryCodePicker = true },
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Select country",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight(),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                )

                TextField(
                    value = state.phoneNumber,
                    onValueChange = { newValue ->
                        val filteredValue = newValue.filter { it.isDigit() }
                        viewModel.setPhoneNumber(filteredValue)
                        viewModel.updateCurrentMask()
                    },
                    modifier = Modifier.weight(0.7f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    visualTransformation = PhoneNumberTransformation(state.currentMask),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true
                )
            }

            if (showCountryCodePicker) {
                CountryPickerDialog(
                    onCountrySelected = { country ->
                        viewModel.setSelectedCountry(country)
                        viewModel.setDialCode(country.code)
                        showCountryCodePicker = false
                        dialCodeValue = TextFieldValue(
                            text = country.code,
                            selection = TextRange(country.code.length)
                        )
                    },
                    countries = state.countries,
                    onDismissRequest = { showCountryCodePicker = false }
                )
            }

            Button(
                onClick = { showConfirmDialog = true },
                enabled = state.dialCode.isNotBlank() && state.phoneNumber.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 80.dp)
                    .height(50.dp)
                    .padding(horizontal = 32.dp)
            ) {
                Text("Отправить код")
            }

            if (showConfirmDialog) {
                ConfirmationDialog(
                    dialCode = state.dialCode,
                    phoneNumber = state.phoneNumber,
                    currentMask = state.currentMask,
                    onConfirm = {
                        showConfirmDialog = false
                        viewModel.sendAuthCode()
                    },
                    onDismiss = { showConfirmDialog = false }
                )
            }
            state.errorMessage?.let {
                ErrorDialog(
                    errorMessage = it,
                    onDismiss = { viewModel.clearError() }
                )
            }
            if (state.showProgressBar) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
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