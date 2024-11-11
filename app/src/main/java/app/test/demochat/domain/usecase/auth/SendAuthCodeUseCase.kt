package app.test.demochat.domain.usecase.auth

import app.test.demochat.data.repository.AuthRepository
import app.test.demochat.domain.network.Result

class SendAuthCodeUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(phone: String): Result<Unit> =
        authRepository.sendAuthCode(phone)
}