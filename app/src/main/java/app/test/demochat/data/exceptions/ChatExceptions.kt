package app.test.demochat.data.exceptions

import java.io.IOException

/**
 * Базовый класс для исключений чата (Иерархия исключений).
 */
sealed class ChatException(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * Unchecked exception (наследуется от RuntimeException через Exception в Kotlin).
 * Используется для ошибок логики, которые не обязательно обрабатывать везде.
 */
class ChatNotFoundException(chatId: String) : ChatException("Чат с id $chatId не найден")

/**
 * Checked-like exception (в Kotlin все исключения unchecked, но мы имитируем 
 * поведение для критических ошибок ввода-вывода).
 */
class MessageDeliveryException(message: String, cause: Throwable) : ChatException(message, cause)

/**
 * Ошибка доступа к локальному хранилищу.
 */
class StorageException(message: String, cause: Throwable) : IOException(message, cause)