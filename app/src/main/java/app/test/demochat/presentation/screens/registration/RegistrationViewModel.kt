package app.test.demochat.presentation.screens.registration

import app.test.demochat.data.local.UserDataStore
import app.test.demochat.data.model.UserParams
import app.test.demochat.data.model.UserProfile
import app.test.demochat.data.model.requests.RegisterRequest
import app.test.demochat.domain.network.Result
import app.test.demochat.domain.usecase.auth.RegisterUserUseCase
import app.test.demochat.presentation.navigation.NavigationProvider
import app.test.demochat.presentation.navigation.Screens
import app.test.demochat.presentation.screens.main.BaseViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegistrationState(
    val userParams: UserParams,
    val name: String = "",
    val username: String = "",
    val messageState: String? = null,
    val showProgressBar: Boolean = false,
)

class RegistrationViewModel(
    userParams: UserParams,
    private val registerUserUseCase: RegisterUserUseCase,
    private val userDataStore: UserDataStore,
    private val navigationProvider: NavigationProvider,
) : BaseViewModel<RegistrationState>(RegistrationState(userParams)) {

    fun setName(name: String) {
        _state.update { it.copy(name = name) }
    }

    fun setUsername(username: String) {
        _state.update { it.copy(username = username) }
    }

    fun register() = launch {
        _state.update { it.copy(showProgressBar = true) }
        val request = RegisterRequest(
            state.value.userParams.phoneNumber,
            state.value.name,
            state.value.username
        )
        when (val result = registerUserUseCase(request)) {
            is Result.Success -> {

                val user = UserProfile(
                    id = state.value.userParams.id,
                    phone = state.value.userParams.phoneNumber,
                    name = state.value.name,
                    username = state.value.username
                )
                userDataStore.saveUserProfile(user)
                navigationProvider.navController.navigate(Screens.ChatList)
            }

            is Result.Error -> {
                _state.update {
                    it.copy(
                        showProgressBar = false,
                        messageState = result.message
                    )
                }
            }
        }
    }
}