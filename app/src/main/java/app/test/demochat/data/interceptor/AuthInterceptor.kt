package app.test.demochat.data.interceptor

import app.test.demochat.data.local.PreferencesManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val preferencesManager: PreferencesManager,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${preferencesManager.getAccessToken()}")
            .build()
        return chain.proceed(request)
    }
}
