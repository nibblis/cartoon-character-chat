package app.test.demochat.data.model.responses

import app.test.demochat.data.model.UserProfile
import com.google.gson.annotations.SerializedName

data class UserProfilePesponce(
    @SerializedName("profile_data")
    val profile: UserProfile
)