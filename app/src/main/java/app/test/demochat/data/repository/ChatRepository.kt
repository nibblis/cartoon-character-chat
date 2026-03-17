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