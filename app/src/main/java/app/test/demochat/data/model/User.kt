package app.test.demochat.data.model

data class UserProfile(
    val id: String,
    val phone: String,
    val name: String,
    val username: String,
    val avatar: String? = null,
    val city: String? = null,
    val birthday: String? = null,
    val about: String? = null
)