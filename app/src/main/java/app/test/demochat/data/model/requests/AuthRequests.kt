package app.test.demochat.data.model.requests

import com.google.gson.annotations.SerializedName

data class SendAuthCodeRequest(
    val phone: String
)

data class CheckAuthCodeRequest(
    val phone: String,
    val code: String
)

data class RefreshTokenRequest(
    @SerializedName("refresh_token")
    val refreshToken: String?
)

data class RegisterRequest(
    val phone: String,
    val name: String,
    val username: String
)