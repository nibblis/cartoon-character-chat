package app.test.demochat.presentation.screens.auth.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.test.demochat.data.model.Country

@Composable
fun CountryPickerDialog(
    onCountrySelected: (Country) -> Unit,
    onDismissRequest: () -> Unit,
    countries: List<Country>
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredCountries = remember(searchQuery) {
        countries.filter { country ->
            country.name.contains(searchQuery, ignoreCase = true) ||
                    country.code.contains(searchQuery) ||
                    country.code.contains(searchQuery, ignoreCase = true)
        }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column {
                // Search field
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder = { Text("Поиск страны или кода") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Поиск"
                        )
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )

                LazyColumn {
                    items(filteredCountries) { country ->
                        ListItem(
                            modifier = Modifier
                                .clickable { onCountrySelected(country) }
                                .fillMaxWidth(),
                            leadingContent = { Text(country.flag) },
                            headlineContent = { Text(country.name) },
                            trailingContent = { Text(country.code) }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}