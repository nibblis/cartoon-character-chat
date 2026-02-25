package app.test.demochat.data.repository

import app.test.demochat.data.api.ApiClient
import app.test.demochat.data.local.PreferencesManager
import app.test.demochat.data.model.requests.CheckAuthCodeRequest
import app.test.demochat.data.model.requests.RegisterRequest
import app.test.demochat.data.model.requests.SendAuthCodeRequest
import app.test.demochat.data.model.responses.AuthResponse
import app.test.demochat.data.model.responses.RegisterResponse
import app.test.demochat.domain.network.Result
import app.test.demochat.domain.network.SafeApiCall

class AuthRepository(
    safeApiCall: SafeApiCall,
    apiClient: ApiClient,
    private val preferencesManager: PreferencesManager,
) : BaseRepository(safeApiCall, apiClient), AuthRepositoryContract {

    override suspend fun hasValidTokens(): Boolean {
        return preferencesManager.getRefreshToken() != null
    }

    override suspend fun sendAuthCode(phone: String): Result<Unit> =
        apiCall(apiCall = { apiClient.sendAuthCode(SendAuthCodeRequest(phone)) })

    override suspend fun checkAuthCode(phone: String, code: String): Result<AuthResponse> =
        apiCall(
            apiCall = { apiClient.checkAuthCode(CheckAuthCodeRequest(phone, code)) },
            onSuccess = { saveTokens(it) }
        )

    override suspend fun register(request: RegisterRequest): Result<RegisterResponse> =
        apiCall(
            apiCall = { apiClient.register(request) },
            onSuccess = { saveTokens(it) }
        )

    private suspend fun saveTokens(response: AuthResponse) {
        preferencesManager.saveTokens(
            response.accessToken,
            response.refreshToken
        )
    }

    private suspend fun saveTokens(response: RegisterResponse) {
        preferencesManager.saveTokens(
            response.accessToken,
            response.refreshToken
        )
    }
}