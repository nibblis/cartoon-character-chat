package app.test.demochat.data.api

import app.test.demochat.data.model.requests.CheckAuthCodeRequest
import app.test.demochat.data.model.requests.RegisterRequest
import app.test.demochat.data.model.requests.SendAuthCodeRequest
import app.test.demochat.data.model.responses.AuthResponse
import app.test.demochat.data.model.responses.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiClient {
    @POST("api/v1/users/send-auth-code/")
    suspend fun sendAuthCode(@Body request: SendAuthCodeRequest): Response<Unit>

    @POST("api/v1/users/check-auth-code/")
    suspend fun checkAuthCode(@Body request: CheckAuthCodeRequest): AuthResponse

    @POST("api/v1/users/register/")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse
}