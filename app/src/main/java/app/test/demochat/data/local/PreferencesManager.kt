package app.test.demochat.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class PreferencesManager(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")
    private val dataStore get() = context.dataStore

    private object PreferencesKeys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    private val _accessToken = MutableStateFlow<String?>(null)
    val accessToken: StateFlow<String?> = _accessToken.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.data.collectLatest { preferences ->
                _accessToken.value = preferences[PreferencesKeys.ACCESS_TOKEN]
            }
        }
    }

    suspend fun saveTokens(accessToken: String?, refreshToken: String?) {
        if (accessToken == null && refreshToken == null) return
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN] = accessToken!!
            preferences[PreferencesKeys.REFRESH_TOKEN] = refreshToken!!
        }
    }

    fun getAccessToken(): String? = accessToken.value

    suspend fun getRefreshToken(): String? =
        dataStore.data.firstOrNull()?.get(PreferencesKeys.REFRESH_TOKEN)

    suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.ACCESS_TOKEN)
            preferences.remove(PreferencesKeys.REFRESH_TOKEN)
        }
    }
}