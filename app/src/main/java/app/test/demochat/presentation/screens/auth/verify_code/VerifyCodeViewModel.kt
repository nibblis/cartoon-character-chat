package app.test.demochat.presentation.screens.auth.verify_code

import androidx.navigation.navOptions
import app.test.demochat.data.local.PreferencesManager
import app.test.demochat.data.model.AuthParams
import app.test.demochat.data.model.UserParams
import app.test.demochat.domain.network.Result
import app.test.demochat.domain.usecase.auth.CheckAuthCodeUseCase
import app.test.demochat.presentation.navigation.NavigationProvider
import app.test.demochat.presentation.navigation.Screens
import app.test.demochat.presentation.screens.main.BaseViewModel
import app.test.demochat.utils.formatByMask
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class VerifyCodeState(
    val authParams: AuthParams,
    val otpCode: String = "",
    val messageState: String? = null,
    val showProgressBar: Boolean = false,
    val shouldShowCodeError: Boolean = false,
)

class VerifyCodeViewModel(
    authParams: AuthParams,
    private val checkAuthCodeUseCase: CheckAuthCodeUseCase,
    private val preferencesManager: PreferencesManager,
    private val navigationProvider: NavigationProvider,
) : BaseViewModel<VerifyCodeState>(VerifyCodeState(authParams)) {

    fun onOtpModified(code: String) {
        _state.update {
            it.copy(
                otpCode = code,
                messageState = null,
                shouldShowCodeError = false,
            )
        }
    }

    fun onOtpEntered() = launch {
        _state.update { it.copy(showProgressBar = true) }

        when (val response = checkAuthCodeUseCase(getPhoneNumber(), state.value.otpCode)) {
            is Result.Success -> {
                preferencesManager.saveTokens(
                    response.data.accessToken,
                    response.data.refreshToken
                )
                if (response.data.isUserExists) {
                    val navOptions = navOptions {
                        popUpTo(navigationProvider.navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                    navigationProvider.navController.navigate(Screens.ChatList, navOptions)
                } else {
                    val navOptions = navOptions {
                        popUpTo(Screens.Auth)
                        launchSingleTop = true
                    }
                    navigationProvider.navController.navigate(
                        Screens.Registration(
                            getUserParams(response.data.userId.orEmpty())
                        ),
                        navOptions
                    )
                }
            }

            is Result.Error -> {
                println("Error checking OTP code: ${response.message}")
                _state.update {
                    it.copy(
                        shouldShowCodeError = true,
                        showProgressBar = false
                    )
                }
            }
        }
    }

    fun onBackPressed() {
        navigationProvider.navController.popBackStack()
    }

    private fun getPhoneNumber(): String {
        return "${state.value.authParams.dialCode}${state.value.authParams.phoneNumber}"
    }

    private fun getUserParams(userId: String): UserParams {
        return UserParams(
            id = userId,
            phoneNumber = "${state.value.authParams.dialCode}${
                formatByMask(
                    mask = state.value.authParams.mask,
                    input = state.value.authParams.phoneNumber
                )
            }"
        )
    }
}