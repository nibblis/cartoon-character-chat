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

    fun getMessages(chatId: String): List<Message> {
        return getMessages().filter { it.chatId == chatId }
    }

    fun sendMessage(chatId: String, text: String, senderId: String) {}
}