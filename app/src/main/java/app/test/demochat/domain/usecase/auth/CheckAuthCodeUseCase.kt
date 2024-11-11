package app.test.demochat.domain.usecase.auth

import app.test.demochat.data.model.responses.AuthResponse
import app.test.demochat.data.repository.AuthRepository
import app.test.demochat.domain.network.Result

class CheckAuthCodeUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(phone: String, code: String): Result<AuthResponse> =
        authRepository.checkAuthCode(phone, code)
}