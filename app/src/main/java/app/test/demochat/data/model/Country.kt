package app.test.demochat.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val code: String,
    val iso: String,
    var name: String,
    var flag: String,
    val masks: List<String>
)