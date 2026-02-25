package app.test.demochat.domain.usecase.auth

import app.test.demochat.data.repository.AuthRepositoryContract
import app.test.demochat.domain.network.Result

class SendAuthCodeUseCase(private val authRepository: AuthRepositoryContract) {
    suspend operator fun invoke(phone: String): Result<Unit> =
        authRepository.sendAuthCode(phone)
}