package app.test.demochat.data.api

import app.test.demochat.data.model.requests.RefreshTokenRequest
import app.test.demochat.data.model.requests.UpdateProfileRequest
import app.test.demochat.data.model.responses.AuthResponse
import app.test.demochat.data.model.responses.UserProfilePesponce
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthenticatedApiClient {

    @POST("api/v1/users/refresh-token/")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): AuthResponse

    @GET("api/v1/users/me/")
    suspend fun getUserProfile(): UserProfilePesponce

    @PUT("api/v1/users/me/")
    suspend fun updateUserProfile(@Body request: UpdateProfileRequest): Response<Unit>
}