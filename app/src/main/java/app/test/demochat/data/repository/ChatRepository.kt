package app.test.demochat.data.repository

import app.test.demochat.data.exceptions.ChatNotFoundException
import app.test.demochat.data.exceptions.StorageException
import app.test.demochat.data.mock.getChats
import app.test.demochat.data.mock.getMessages
import app.test.demochat.data.model.ChatItem
import app.test.demochat.data.model.Message
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class ChatRepository {

    /**
     * Демонстрация Unchecked exception для логических ошибок.
     * Вместо возврата null, выбрасываем кастомное исключение, если чат не найден.
     */
    fun getChatById(chatId: String): ChatItem {
        return getChats().firstOrNull { it.id == chatId } 
            ?: throw ChatNotFoundException(chatId)
    }

    fun getChatList(): List<ChatItem> {
        return getChats()
    }

    /**
     * Демонстрация навыка работы с коллекциями и Sequences.
     * 
     * Использование asSequence() здесь оправдано, так как выполняется длинная цепочка
     * преобразований (filter -> map -> sortedBy -> take).
     * 
     * Преимущества:
     * 1. Memory Profit: Не создаются промежуточные списки List для каждого шага.
     * 2. Performance: Благодаря lazy evaluation (ленивые вычисления), 
     *    если в take(10) условие выполняется быстро, остаток списка не обрабатывается.
     * 3. Readability: Четкая декларативная цепочка преобразований данных.
     */
    fun getFilteredAndSortedChats(query: String): List<ChatItem> {
        return getChats()
            .asSequence()
            .filter { it.title.contains(query, ignoreCase = true) }
            .filter { it.unreadCount >= 0 }
            .map { it.copy(title = it.title.trim()) }
            .sortedByDescending { it.timestamp }
            .take(10)
            .toList()
    }

    /**
     * Демонстрация Generics и PECS (Producer Extends, Consumer Super).
     * 
     * @param items Источник данных (Producer). В Kotlin 'out' соответствует '? extends' в Java.
     *              Это позволяет передавать List<SpecificChatItem>, где SpecificChatItem наследуется от ChatItem.
     * @param transformer Функция трансформации.
     */
    fun <T : ChatItem, R> processGenericItems(
        items: List<out T>,
        transformer: (T) -> R
    ): List<R> {
        return items.asSequence()
            .map(transformer)
            .toList()
    }

    /**
     * Демонстрация предотвращения лишнего боксинга (boxing/unboxing).
     * Использование специализированных функций агрегации (sumOf) эффективнее, чем map { it.unreadCount }.sum(),
     * так как не создает промежуточный список List<Int> с объектами-обертками Integer.
     */
    fun getTotalUnreadCount(): Int {
        return getChats().sumOf { it.unreadCount }
    }

    /**
     * Демонстрация ExecutorService, работы с пулом потоков и JMM.
     * 
     * JMM (Java Memory Model) гарантирует видимость (visibility) и упорядоченность 
     * (ordering) операций. Здесь мы используем AtomicInteger для обеспечения 
     * атомарности изменений в многопоточной среде без блокировок (lock-free), 
     * что исключает возникновение дедлоков (deadlocks) и состояний гонки (race conditions).
     */
    fun parallelMessageAnalysis(chatId: String): Int {
        val messages = getMessages(chatId)
        val executor = Executors.newFixedThreadPool(4)
        val wordCount = AtomicInteger(0)

        try {
            val tasks = messages.map { message ->
                Callable {
                    val count = message.text.split(" ").size
                    wordCount.addAndGet(count)
                }
            }

            // Выполнение задач с таймаутом (корректная обработка времени)
            val futures = executor.invokeAll(tasks, 5, TimeUnit.SECONDS)
            
            // Проверка на отмену по таймауту
            futures.forEach { if (it.isCancelled) println("Задача была отменена по таймауту") }

        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt() // Корректная обработка прерывания
        } finally {
            // Обязательное завершение работы пула (предотвращение утечек ресурсов)
            executor.shutdown()
            if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                executor.shutdownNow()
            }
        }
        
        return wordCount.get()
    }

    /**
     * Демонстрация корректного освобождения ресурсов и обработки исключений.
     * 
     * В Kotlin аналогом try-with-resources является функция .use().
     * Она гарантирует закрытие Closeable ресурса даже при возникновении исключения.
     */
    fun exportChatHistory(chatId: String, targetFile: File) {
        val messages = getMessages().filter { it.chatId == chatId }
        
        try {
            // try-with-resources (через use)
            FileOutputStream(targetFile).use { output ->
                messages.forEach { msg ->
                    val line = "${msg.senderId}: ${msg.text}\n"
                    output.write(line.toByteArray())
                }
            }
        } catch (e: IOException) {
            // Оборачивание системного исключения в доменное (Checked -> Custom)
            throw StorageException("Не удалось экспортировать историю чата $chatId", e)
        }
    }

    fun getMessages(chatId: String): List<Message> {
        return getMessages().filter { it.chatId == chatId }
    }

    fun getMessagesBySender(chatId: String): Map<String, List<Message>> {
        return getMessages(chatId)
            .groupBy { it.senderId }
    }

    fun sendMessage(chatId: String, text: String, senderId: String) {}
}