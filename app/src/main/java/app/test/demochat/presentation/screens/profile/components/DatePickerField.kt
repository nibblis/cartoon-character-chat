package app.test.demochat.presentation.screens.profile.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import app.test.demochat.utils.DATE_INPUT_FORMAT
import app.test.demochat.utils.DATE_OUTPUT_FORMAT
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDatePicker by remember { mutableStateOf(false) }

    val initialDate = try {
        val format = SimpleDateFormat(DATE_INPUT_FORMAT, Locale.getDefault())
        if (value.isNotBlank()) format.parse(value)?.time ?: System.currentTimeMillis()
        else System.currentTimeMillis()
    } catch (e: Exception) {
        System.currentTimeMillis()
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate
    )

    OutlinedTextField(
        value = if (value.isNotBlank()) {
            try {
                val inputFormat = SimpleDateFormat(DATE_INPUT_FORMAT, Locale.getDefault())
                val outputFormat = SimpleDateFormat(DATE_OUTPUT_FORMAT, Locale("ru"))
                val date = inputFormat.parse(value)
                outputFormat.format(date)
            } catch (e: Exception) {
                value
            }
        } else "",
        onValueChange = { },
        label = { Text("Дата рождения") },
        modifier = modifier,
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Выбрать дату"
                )
            }
        }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { dateMillis ->
                            val calendar = Calendar.getInstance()
                            calendar.timeInMillis = dateMillis
                            val format = SimpleDateFormat(DATE_INPUT_FORMAT, Locale.getDefault())
                            onValueChange(format.format(calendar.time))
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false
            )
        }
    }
}