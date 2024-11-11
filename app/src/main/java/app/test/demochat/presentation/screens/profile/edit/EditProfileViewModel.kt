package app.test.demochat.presentation.screens.profile.edit

import app.test.demochat.data.model.UserProfile
import app.test.demochat.data.model.requests.UpdateProfileRequest
import app.test.demochat.data.model.requests.UserAvatarRequest
import app.test.demochat.domain.network.Result
import app.test.demochat.domain.usecase.user.GetUserProfileUseCase
import app.test.demochat.domain.usecase.user.UpdateUserProfileUseCase
import app.test.demochat.presentation.navigation.NavigationProvider
import app.test.demochat.presentation.screens.main.BaseViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EditProfileState(
    val profile: UserProfile? = null,
    val avatar: String? = null,
    val showProgressBar: Boolean = false,
    val messageState: String? = null,
)

class EditProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val navigationProvider: NavigationProvider,
) : BaseViewModel<EditProfileState>(EditProfileState()) {

    private var filename = ""
    private var avatarBase64 = ""

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

    fun setName(name: String) {
        _state.update {
            it.copy(profile = it.profile?.copy(name = name))
        }
    }

    fun setСity(city: String) {
        _state.update {
            it.copy(profile = it.profile?.copy(city = city))
        }
    }

    fun setAbout(about: String) {
        _state.update {
            it.copy(profile = it.profile?.copy(about = about))
        }
    }


    fun setBirthday(birthday: String) {
        _state.update {
            it.copy(profile = it.profile?.copy(birthday = birthday))
        }
    }

    fun setAvatar(filename: String, base64: String) {
        this.filename = filename
        this.avatarBase64 = base64
        launch {
            updateUserProfileUseCase.saveUserAvatar(base64)
        }
    }

    fun onBackPressed() {
        navigationProvider.navController.popBackStack()
    }

    fun updateProfile() = launch {
        val profile = _state.value.profile ?: return@launch
        val request = UpdateProfileRequest(
            name = profile.name,
            username = profile.username,
            city = profile.city?.takeIf { it.isNotBlank() },
            birthday = profile.birthday?.takeIf { it.isNotBlank() },
            about = profile.about?.takeIf { it.isNotBlank() },
            avatar = getUserAvatarRequest()
        )
        try {
            _state.update { it.copy(showProgressBar = true) }
            when (val response = updateUserProfileUseCase(request)) {
                is Result.Success -> {
                    _state.update { it.copy(showProgressBar = false) }
                    updateUserProfileUseCase.saveUserProfile(profile)
                    onBackPressed()
                }

                is Result.Error -> {
                    _state.update { it.copy(messageState = response.message) }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            _state.update { it.copy(showProgressBar = false) }
        }
    }

    private fun getUserAvatarRequest(): UserAvatarRequest? {
        if (filename.isNotBlank() && avatarBase64.isNotBlank()) {
            return UserAvatarRequest(filename, avatarBase64)
        }
        return null
    }
}
