package app.test.demochat.data.repository

import app.test.demochat.data.api.AuthenticatedApiClient
import app.test.demochat.data.local.UserDataStore
import app.test.demochat.data.model.UserProfile
import app.test.demochat.data.model.requests.UpdateProfileRequest
import app.test.demochat.domain.network.Result
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val apiClient: AuthenticatedApiClient,
    private val userDataStore: UserDataStore,
) {
    suspend fun getUserProfile(): Result<UserProfile> = try {
        val response = apiClient.getUserProfile()
        userDataStore.saveUserProfile(response.profile)
        Result.Success(response.profile)
    } catch (e: Exception) {
        Result.Error(e.message ?: "Failed to get user profile")
    }

    suspend fun updateUserProfile(request: UpdateProfileRequest): Result<Unit> = try {
        apiClient.updateUserProfile(request)
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.message ?: "Failed to update user profile")
    }

    fun getCachedUserProfile(): Flow<UserProfile?> =
        userDataStore.getUserProfile()

    suspend fun saveUserProfile(profile: UserProfile) {
        userDataStore.saveUserProfile(profile)
    }

    suspend fun saveUserAvatar(avatar: String) {
        userDataStore.saveUserAvatar(avatar)
    }

    fun getUserAvatar(): Flow<String?> =
        userDataStore.getUserAvatar()

}