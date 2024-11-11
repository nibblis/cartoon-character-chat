package app.test.demochat.presentation.screens.profile

import androidx.navigation.navOptions
import app.test.demochat.data.local.PreferencesManager
import app.test.demochat.data.model.UserProfile
import app.test.demochat.domain.network.Result
import app.test.demochat.domain.usecase.user.GetUserProfileUseCase
import app.test.demochat.presentation.navigation.NavigationProvider
import app.test.demochat.presentation.navigation.Screens
import app.test.demochat.presentation.screens.main.BaseViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileState(
    val profile: UserProfile? = null,
    val avatar: String? = null,
    val showProgressBar: Boolean = false,
    val messageState: String? = null,
)

class ProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val navigationProvider: NavigationProvider,
    private val preferencesManager: PreferencesManager
) : BaseViewModel<ProfileState>(ProfileState()) {

    init {
        loadProfile()
        loadUserAvatar()
    }

    private fun loadProfile() = launch {
        _state.update { it.copy(showProgressBar = true) }
        getUserProfileUseCase.getCached().collect { cachedProfile ->
            if (cachedProfile != null) {
                _state.update { it.copy(profile = cachedProfile, showProgressBar = false) }
            } else {
                updateProfileFromRepository()
            }
        }
    }

    private fun loadUserAvatar() = launch {
        getUserProfileUseCase.getUserAvatar().collect { avatar ->
            _state.update { it.copy(avatar = avatar) }
        }
    }

    private suspend fun updateProfileFromRepository() {
        try {
            when (val response = getUserProfileUseCase()) {
                is Result.Success -> {
                    _state.update { it.copy(showProgressBar = false) }
                }

                is Result.Error -> {
                    _state.update {
                        it.copy(
                            messageState = response.message,
                            showProgressBar = false
                        )
                    }
                }
            }
        } catch (e: Exception) {
            _state.update { it.copy(showProgressBar = false) }
        }
    }

    fun onBackPressed() {
        navigationProvider.navController.popBackStack()
    }

    fun navigateToEditProfile() {
        navigationProvider.navController.navigate(Screens.EditProfile)
    }

    fun logout() {
        val navOptions = navOptions {
            popUpTo(navigationProvider.navController.graph.startDestinationId) {
                inclusive = true
            }
            launchSingleTop = true
        }
        navigationProvider.navController.navigate(Screens.Auth, navOptions)
        launch {
            preferencesManager.clearTokens()
        }
    }
}
