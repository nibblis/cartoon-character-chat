package app.test.demochat.presentation.screens.main

import app.test.demochat.data.repository.AuthRepositoryContract
import app.test.demochat.presentation.navigation.INavigation
import kotlinx.coroutines.launch

sealed class AppState {
    object Loading : AppState()
    object Authenticated : AppState()
    object NotAuthenticated : AppState()
}

class MainViewModel(
    private val authRepository: AuthRepositoryContract,
    internal val navigatables: List<INavigation>,
) : BaseViewModel<AppState>(AppState.Loading) {

    init {
        checkAuthState()
    }

    private fun checkAuthState() = launch {
        _state.value = AppState.Loading

        try {
            val hasTokens = authRepository.hasValidTokens()
            if (!hasTokens) {
                _state.value = AppState.NotAuthenticated
                return@launch
            }

            _state.value = AppState.Authenticated
        } catch (e: Exception) {
            _state.value = AppState.NotAuthenticated
        }
    }
}