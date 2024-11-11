package app.test.demochat.data.model.responses

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("user_id")
    val userId: String,
)