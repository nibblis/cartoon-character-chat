package app.test.demochat.domain.network

interface ErrorHandler {
    fun handleException(throwable: Throwable): String
}