package app.test.demochat.presentation.screens.chatList

import app.test.demochat.data.model.ChatItem
import app.test.demochat.data.repository.ChatRepository
import app.test.demochat.presentation.navigation.NavigationProvider
import app.test.demochat.presentation.navigation.Screens
import app.test.demochat.presentation.screens.main.BaseViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatListState(
    val chatList: List<ChatItem> = emptyList()
)

class ChatListViewModel(
    private val navigationProvider: NavigationProvider,
    private val repository: ChatRepository,
) : BaseViewModel<ChatListState>(ChatListState()) {

    init {
        loadChats()
    }

    private fun loadChats() = launch {
        try {
            val chatList = repository.getChatList()
            _state.update { it.copy(chatList = chatList) }
        } catch (e: Exception) {
            println("Error loading chats: ${e.message}")
        }
    }

    fun navigateToChatScreen(chatId: String) {
        navigationProvider.navController.navigate(Screens.Chat(chatId))
    }

    fun navigateToProfileScreen() {
        navigationProvider.navController.navigate(Screens.Profile)
    }
}