package app.test.demochat.presentation.navigation

import android.os.Bundle
import androidx.navigation.NavType
import app.test.demochat.data.model.AuthParams
import app.test.demochat.data.model.UserParams
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
sealed class Screens {
    @Serializable
    data object Auth : Screens()
    @Serializable
    data class VerifyCode(val params: AuthParams) : Screens()
    @Serializable
    data class Registration(val params: UserParams) : Screens()
    @Serializable
    data object ChatList : Screens()
    @Serializable
    data class Chat(val chatId: String) : Screens()
    @Serializable
    data object Profile : Screens()
    @Serializable
    data object EditProfile : Screens()
}

inline fun <reified T : Any> serializableType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String): T? {
        return bundle.getString(key)?.let<String, T>(json::decodeFromString)
    }

    override fun parseValue(value: String): T {
        return json.decodeFromString(value)
    }

    override fun serializeAsValue(value: T): String {
        return json.encodeToString(value)
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, json.encodeToString(value))
    }
}