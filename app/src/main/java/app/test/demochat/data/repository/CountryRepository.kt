package app.test.demochat.data.repository

import android.content.Context
import app.test.demochat.data.model.Country
import kotlinx.serialization.json.Json

class CountryRepository(
    private val context: Context
) {
    fun getCountryList(): List<Country> {
        val assetManager = context.assets
        val inputStream = assetManager.open("countries.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val countries = Json.decodeFromString<List<Country>>(jsonString)
        return countries
    }
}