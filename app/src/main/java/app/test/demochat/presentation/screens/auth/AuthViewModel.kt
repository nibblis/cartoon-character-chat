package app.test.demochat.presentation.screens.auth

import androidx.lifecycle.viewModelScope
import app.test.demochat.data.model.AuthParams
import app.test.demochat.data.model.Country
import app.test.demochat.data.repository.CountryRepository
import app.test.demochat.domain.network.Result
import app.test.demochat.domain.usecase.auth.SendAuthCodeUseCase
import app.test.demochat.presentation.navigation.NavigationProvider
import app.test.demochat.presentation.navigation.Screens
import app.test.demochat.presentation.screens.main.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

private const val DEFAULT_MASK = "(###)###-##-##"

/**
 * MVI ViewModel.
 * Демонстрирует односторонний поток данных (UDF):
 * Intent -> Reducer -> State
 */
class AuthViewModel(
    private val sendAuthCodeUseCase: SendAuthCodeUseCase,
    private val countryRepository: CountryRepository,
    private val navigationProvider: NavigationProvider,
) : BaseViewModel<AuthState>(AuthState()) {

    // Канал для разовых эффектов (Side Effects) - MVI Effect
    private val _effects = Channel<AuthEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    private var countryMasks: Map<String, List<String>> = emptyMap()
    private var masks = listOf(DEFAULT_MASK)

    init {
        handleIntent(AuthIntent.LoadCountries)
    }

    /**
     * Единственная точка входа для всех действий пользователя (MVI Intent)
     */
    fun handleIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.LoadCountries -> loadCountryList()
            is AuthIntent.SelectCountry -> reduceSelectCountry(intent.country)
            is AuthIntent.UpdateDialCode -> _state.update { it.copy(dialCode = intent.code) }
            is AuthIntent.UpdatePhoneNumber -> reduceUpdatePhoneNumber(intent.number)
            is AuthIntent.SendCode -> sendAuthCode()
            is AuthIntent.ClearError -> _state.update { it.copy(errorMessage = null) }
        }
    }

    private fun loadCountryList() = launch {
        try {
            val countries = countryRepository.getCountryList()
            countryMasks = countries.associate { country ->
                country.iso to country.masks
            }
            val selectedCountry = countries.firstOrNull {
                it.iso == Locale.getDefault().country
            }
            
            // Reducer: Обновление состояния (Единый источник истины)
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

    private fun reduceSelectCountry(country: Country?) {
        _state.update { it.copy(selectedCountry = country) }
        updateMasks()
        updateCurrentMask()
    }

    private fun reduceUpdatePhoneNumber(number: String) {
        _state.update { it.copy(phoneNumber = number) }
        updateCurrentMask()
    }

    private fun updateCurrentMask() {
        val newMask = masks.firstOrNull { mask ->
            val digitCount = mask.count { it == '#' }
            digitCount >= state.value.phoneNumber.length
        } ?: masks.first()
        _state.update { it.copy(currentMask = newMask) }
    }

    private fun sendAuthCode() = launch {
        _state.update { it.copy(showProgressBar = true) }
        val result = sendAuthCodeUseCase("${state.value.dialCode}${state.value.phoneNumber}")
        
        _state.update { it.copy(showProgressBar = false) }
        
        when (result) {
            is Result.Success -> {
                // Вызов эффекта навигации (Side Effect)
                viewModelScope.launch {
                    _effects.send(AuthEffect.NavigateToVerify(
                        dialCode = state.value.dialCode,
                        phoneNumber = state.value.phoneNumber,
                        mask = state.value.currentMask.replace("#", "%23")
                    ))
                }
                
                // Традиционная навигация для совместимости с текущим кодом
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
                _state.update { it.copy(errorMessage = result.message) }
            }
        }
    }

    private fun updateMasks() {
        masks = countryMasks[state.value.selectedCountry?.iso] ?: listOf(DEFAULT_MASK)
        updateCurrentMask()
    }
}