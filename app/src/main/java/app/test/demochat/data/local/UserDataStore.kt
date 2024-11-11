package app.test.demochat.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import app.test.demochat.data.model.UserProfile
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserDataStore(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")
    private val dataStore get() = context.dataStore

    private object PreferencesKeys {
        val USER_PROFILE = stringPreferencesKey("user_profile")
        val USER_AVATAR = stringPreferencesKey("user_avatar")
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_PROFILE] =
                Gson().toJson(profile)
        }
    }

    fun getUserProfile(): Flow<UserProfile?> =
        dataStore.data.map { preferences ->
            preferences[PreferencesKeys.USER_PROFILE]?.let {
                Gson().fromJson(it, UserProfile::class.java)
            }
        }

    suspend fun saveUserAvatar(avatar: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_AVATAR] = avatar
        }
    }

    fun getUserAvatar(): Flow<String?> =
        dataStore.data.map { preferences ->
            preferences[PreferencesKeys.USER_AVATAR]
        }
}