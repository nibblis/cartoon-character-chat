package app.test.demochat.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserParams(
    val id: String = "",
    val phoneNumber: String = "",
)