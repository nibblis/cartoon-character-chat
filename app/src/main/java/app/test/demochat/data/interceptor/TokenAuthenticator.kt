package app.test.demochat.data.interceptor

import app.test.demochat.data.api.AuthenticatedApiClient
import app.test.demochat.data.local.PreferencesManager
import app.test.demochat.data.model.requests.RefreshTokenRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val preferencesManager: PreferencesManager,
    private val authApi: Lazy<AuthenticatedApiClient>
) : Authenticator {

    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 3) {
            return null
        }

        return runBlocking {
            mutex.withLock {
                try {
                    val refreshToken = preferencesManager.getRefreshToken() ?: return@withLock null

                    val newTokens = withContext(Dispatchers.IO) {
                        authApi.value.refreshToken(RefreshTokenRequest(refreshToken))
                    }

                    preferencesManager.saveTokens(newTokens.accessToken, newTokens.refreshToken)

                    response.request.newBuilder()
                        .header("Authorization", "Bearer ${newTokens.accessToken}")
                        .build()
                } catch (e: Exception) {
                    preferencesManager.clearTokens()
                    null
                }
            }
        }
    }

    private fun responseCount(response: Response): Int {
        var result = 1
        var priorResponse = response.priorResponse
        while (priorResponse != null) {
            result++
            priorResponse = priorResponse.priorResponse
        }
        return result
    }
}