package app.test.demochat.data.repository

import app.test.demochat.data.model.requests.RegisterRequest
import app.test.demochat.data.model.responses.AuthResponse
import app.test.demochat.data.model.responses.RegisterResponse
import app.test.demochat.domain.network.Result

interface AuthRepositoryContract {
    suspend fun hasValidTokens(): Boolean
    suspend fun sendAuthCode(phone: String): Result<Unit>
    suspend fun checkAuthCode(phone: String, code: String): Result<AuthResponse>
    suspend fun register(request: RegisterRequest): Result<RegisterResponse>
}