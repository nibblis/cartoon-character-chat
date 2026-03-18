package app.test.demochat.presentation.screens.auth

import app.test.demochat.data.model.Country

/**
 * MVI: State - Единый источник истины для UI
 */
data class AuthState(
    val countries: List<Country> = emptyList(),
    val currentMask: String = "",
    val selectedCountry: Country? = null,
    val dialCode: String = "",
    val phoneNumber: String = "",
    val showProgressBar: Boolean = false,
    val errorMessage: String? = null,
)

/**
 * MVI: Intent (Actions) - Намерения пользователя
 */
sealed class AuthIntent {
    object LoadCountries : AuthIntent()
    data class SelectCountry(val country: Country?) : AuthIntent()
    data class UpdateDialCode(val code: String) : AuthIntent()
    data class UpdatePhoneNumber(val number: String) : AuthIntent()
    object SendCode : AuthIntent()
    object ClearError : AuthIntent()
}

/**
 * MVI: Effect (Side Effects) - Разовые события (навигация, снекбары)
 */
sealed class AuthEffect {
    data class NavigateToVerify(val dialCode: String, val phoneNumber: String, val mask: String) : AuthEffect()
    data class ShowError(val message: String) : AuthEffect()
}