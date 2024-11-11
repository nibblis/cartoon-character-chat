package app.test.demochat.domain.usecase.user

import app.test.demochat.data.model.UserProfile
import app.test.demochat.data.repository.UserRepository
import app.test.demochat.domain.network.Result
import kotlinx.coroutines.flow.Flow

class GetUserProfileUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Result<UserProfile> =
        userRepository.getUserProfile()

    fun getCached(): Flow<UserProfile?> =
        userRepository.getCachedUserProfile()

    fun getUserAvatar(): Flow<String?> =
        userRepository.getUserAvatar()

}