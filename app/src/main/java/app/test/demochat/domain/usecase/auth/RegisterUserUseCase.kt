package app.test.demochat.domain.usecase.auth

import app.test.demochat.data.model.requests.RegisterRequest
import app.test.demochat.data.model.responses.RegisterResponse
import app.test.demochat.data.repository.AuthRepository
import app.test.demochat.domain.network.Result

class RegisterUserUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(request: RegisterRequest): Result<RegisterResponse> =
        authRepository.register(request)
}