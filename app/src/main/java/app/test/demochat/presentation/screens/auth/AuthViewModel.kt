package app.test.demochat.presentation.screens.auth

import app.test.demochat.data.model.AuthParams
import app.test.demochat.data.model.Country
import app.test.demochat.data.repository.CountryRepository
import app.test.demochat.domain.network.Result
import app.test.demochat.domain.usecase.auth.SendAuthCodeUseCase
import app.test.demochat.presentation.navigation.NavigationProvider
import app.test.demochat.presentation.navigation.Screens
import app.test.demochat.presentation.screens.main.BaseViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

private const val DEFAULT_MASK = "(###)###-##-##"

data class AuthState(
    val countries: List<Country> = emptyList(),
    val currentMask: String = "",
    val selectedCountry: Country? = null,
    val dialCode: String = "",
    val phoneNumber: String = "",
    val showProgressBar: Boolean = false,
    val errorMessage: String? = null,
)

class AuthViewModel(
    private val sendAuthCodeUseCase: SendAuthCodeUseCase,
    private val countryRepository: CountryRepository,
    private val navigationProvider: NavigationProvider,
) : BaseViewModel<AuthState>(AuthState()) {

    var countryMasks: Map<String, List<String>> = emptyMap()
    var masks = listOf(DEFAULT_MASK)

    init {
        loadCountryList()
    }

    private fun loadCountryList() {
        launch {
            try {
                val countries = countryRepository.getCountryList()
                countryMasks = countries.associate { country ->
                    country.iso to country.masks
                }
                val selectedCountry = countries.firstOrNull {
                    it.iso == Locale.getDefault().country
                }
                _state.update {
                    it.copy(
                        countries = countries,
                        selectedCountry = selectedCountry,
                        dialCode = selectedCountry?.code.orEmpty()
                    )
                }
                updateMasks()
            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    fun setSelectedCountry(country: Country?) {
        _state.update { it.copy(selectedCountry = country) }
        updateMasks()
        updateCurrentMask()
    }

    fun setDialCode(dialCode: String) {
        _state.update { it.copy(dialCode = dialCode) }
    }

    fun setPhoneNumber(phoneNumber: String) {
        _state.update { it.copy(phoneNumber = phoneNumber) }
    }

    fun updateCurrentMask() {
        val newMask = masks.firstOrNull { mask ->
            val digitCount = mask.count { it == '#' }
            digitCount >= state.value.phoneNumber.length
        } ?: masks.first()
        _state.update { it.copy(currentMask = newMask) }
    }

    fun sendAuthCode() = launch {
        _state.update { it.copy(showProgressBar = true) }
        when (val result =
            sendAuthCodeUseCase("${state.value.dialCode}${state.value.phoneNumber}")) {
            is Result.Success -> {
                _state.update { it.copy(showProgressBar = false) }
                navigationProvider.navController.navigate(
                    Screens.VerifyCode(
                        AuthParams(
                            dialCode = state.value.dialCode,
                            phoneNumber = state.value.phoneNumber,
                            mask = state.value.currentMask.replace("#", "%23")
                        )
                    )
                )
            }

            is Result.Error -> {
                _state.update { it.copy(errorMessage = result.message, showProgressBar = false) }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }

    private fun updateMasks() {
        masks = countryMasks[state.value.selectedCountry?.iso] ?: listOf(DEFAULT_MASK)
        updateCurrentMask()
    }
}