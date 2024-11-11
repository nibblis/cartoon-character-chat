package app.test.demochat.domain.usecase.user

import app.test.demochat.data.model.UserProfile
import app.test.demochat.data.model.requests.UpdateProfileRequest
import app.test.demochat.data.repository.UserRepository
import app.test.demochat.domain.network.Result

class UpdateUserProfileUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(request: UpdateProfileRequest): Result<Unit> =
        userRepository.updateUserProfile(request)

    suspend fun saveUserProfile(profile: UserProfile) {
        userRepository.saveUserProfile(profile)
    }

    suspend fun saveUserAvatar(avatar: String) {
        userRepository.saveUserAvatar(avatar)
    }
}