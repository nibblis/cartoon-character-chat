package app.test.demochat.domain.network

class SafeApiCall(private val errorHandler: ErrorHandler) {
    suspend fun <T> execute(
        apiCall: suspend () -> T,
        onSuccess: (suspend (T) -> Unit)? = null
    ): Result<T> {
        return try {
            val response = apiCall()
            onSuccess?.invoke(response)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(errorHandler.handleException(e))
        }
    }
}