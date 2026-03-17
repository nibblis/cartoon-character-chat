package app.test.demochat.data.repository

import app.test.demochat.data.mock.getChats
import app.test.demochat.data.mock.getMessages
import app.test.demochat.data.model.ChatItem
import app.test.demochat.data.model.Message

class ChatRepository {

    fun getChaById(chatId: String): ChatItem? {
        return getChats().firstOrNull { it.id == chatId }
    }

    fun getChatList(): List<ChatItem> {
        return getChats()
    }

    /**
     * Возвращает отфильтрованный по названию и отсортированный по дате список чатов.
     * Использует Sequence для оптимизации производительности при обработке цепочки преобразований.
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
     * Использование специализированных функций агрегации (sumOf) эффективнее, чем map { it.int }.sum(),
     * так как не создает промежуточный список List<Int> с объектами-обертками.
     */
    fun getTotalUnreadCount(): Int {
        return getChats().sumOf { it.unreadCount }
    }

    fun getMessages(chatId: String): List<Message> {
        return getMessages().filter { it.chatId == chatId }
    }

    /**
     * Группирует сообщения в конкретном чате по отправителям.
     */
    fun getMessagesBySender(chatId: String): Map<String, List<Message>> {
        return getMessages(chatId)
            .groupBy { it.senderId }
    }

    fun sendMessage(chatId: String, text: String, senderId: String) {}
}