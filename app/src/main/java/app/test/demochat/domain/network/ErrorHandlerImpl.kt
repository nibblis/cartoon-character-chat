package app.test.demochat.domain.network

import app.test.demochat.data.model.responses.ErrorResponse
import com.google.gson.Gson
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorHandlerImpl(private val gson: Gson) : ErrorHandler {
    override fun handleException(throwable: Throwable): String {
        return when (throwable) {
            is HttpException -> {
                try {
                    val errorBody = throwable.response()?.errorBody()?.string()
                    val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                    errorResponse.detail.message
                } catch (e: Exception) {
                    "Неизвестная ошибка сервера"
                }
            }

            is UnknownHostException -> "Проверьте подключение к интернету"
            is ConnectException -> "Не удалось подключиться к серверу"
            is SocketTimeoutException -> "Превышено время ожидания"
            else -> throwable.message ?: "Неизвестная ошибка"
        }
    }
}