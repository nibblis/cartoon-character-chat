package app.test.demochat.data.repository

import app.test.demochat.data.local.PreferencesManager
import app.test.demochat.data.model.requests.RegisterRequest
import app.test.demochat.data.model.responses.AuthResponse
import app.test.demochat.data.model.responses.RegisterResponse
import app.test.demochat.domain.network.Result
import kotlinx.coroutines.delay

class FakeAuthRepository(
    private val preferencesManager: PreferencesManager
) : AuthRepositoryContract {

    companion object {
        private const val TEST_OTP = "111111"
        private const val MOCK_ACCESS = "mock_access_token"
        private const val MOCK_REFRESH = "mock_refresh_token"
    }

    override suspend fun hasValidTokens(): Boolean {
        return preferencesManager.getRefreshToken() != null
    }

    override suspend fun sendAuthCode(phone: String): Result<Unit> {
        delay(600)
        return Result.Success(Unit)
    }

    override suspend fun checkAuthCode(phone: String, code: String): Result<AuthResponse> {
        delay(600)

        return if (code == TEST_OTP) {

            val response = AuthResponse(
                accessToken = MOCK_ACCESS,
                refreshToken = MOCK_REFRESH,
                userId = "1",
                isUserExists = true
            )

            preferencesManager.saveTokens(
                response.accessToken,
                response.refreshToken
            )

            Result.Success(response)

        } else {
            Result.Error("Неверный код")
        }
    }

    override suspend fun register(request: RegisterRequest): Result<RegisterResponse> {
        delay(600)

        val response = RegisterResponse(
            accessToken = MOCK_ACCESS,
            refreshToken = MOCK_REFRESH,
            userId = "1"
        )

        preferencesManager.saveTokens(
            response.accessToken,
            response.refreshToken
        )

        return Result.Success(response)
    }
}