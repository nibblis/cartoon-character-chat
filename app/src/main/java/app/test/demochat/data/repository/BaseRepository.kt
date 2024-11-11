package app.test.demochat.data.repository

import app.test.demochat.data.api.ApiClient
import app.test.demochat.domain.network.Result
import app.test.demochat.domain.network.SafeApiCall

abstract class BaseRepository(
    protected val safeApiCall: SafeApiCall,
    protected val apiClient: ApiClient,
) {
    protected suspend fun <T> apiCall(
        apiCall: suspend () -> T,
        onSuccess: (suspend (T) -> Unit)? = null,
    ): Result<T> = safeApiCall.execute(apiCall, onSuccess)
}