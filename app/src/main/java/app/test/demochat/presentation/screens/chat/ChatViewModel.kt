package app.test.demochat.presentation.screens.chat

import app.test.demochat.data.model.Message
import app.test.demochat.data.repository.ChatRepository
import app.test.demochat.presentation.navigation.NavigationProvider
import app.test.demochat.presentation.screens.main.BaseViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatState(
    val messages: List<Message> = emptyList(),
    val chatName: String = "",
    val sendingMessage: Boolean = false,
    val currentUser: String = "cartman"
)

class ChatViewModel(
    private val chatId: String,
    private val navigationProvider: NavigationProvider,
    private val repository: ChatRepository,
) : BaseViewModel<ChatState>(ChatState()) {

    init {
        loadMessages(chatId)
    }

    private fun loadMessages(chatId: String) = launch {
        try {
            val messages = repository.getMessages(chatId)
            val chat = repository.getChaById(chatId)
            _state.update { it.copy(messages = messages, chatName = chat?.title ?: "") }

        } catch (e: Exception) {
            println("Error loading messages: ${e.message}")
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        launch {
            _state.update { it.copy(sendingMessage = true) }
            try {
                repository.sendMessage(
                    chatId = chatId,
                    text = text,
                    senderId = state.value.currentUser
                )
                loadMessages(chatId)
            } catch (e: Exception) {
                println("Error sending message: ${e.message}")
            } finally {
                _state.update { it.copy(sendingMessage = false) }
            }
        }
    }

    fun onBackPressed() {
        navigationProvider.navController.popBackStack()
    }
}