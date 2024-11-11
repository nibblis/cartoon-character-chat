package app.test.demochat.data.model.requests

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest(
    val name: String,
    val username: String,
    val city: String?,
    val birthday: String?,
    val about: String?,
    val avatar: UserAvatarRequest?,
)

data class UserAvatarRequest(
    @SerializedName("filename")
    val name: String,
    @SerializedName("base_64")
    val base_64: String,
)