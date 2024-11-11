package app.test.demochat.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthParams(
    val dialCode: String = "",
    val phoneNumber: String = "",
    val mask: String = ""
)