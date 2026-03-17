package app.test.demochat.presentation.screens.chat

import androidx.lifecycle.viewModelScope
import app.test.demochat.data.model.Message
import app.test.demochat.data.repository.ChatRepository
import app.test.demochat.presentation.navigation.NavigationProvider
import app.test.demochat.presentation.screens.main.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatState(
    val messages: List<Message> = emptyList(),
    val chatName: String = "",
    val sendingMessage: Boolean = false,
    val currentUser: String = "cartman",
    val isLoading: Boolean = false,
    val error: String? = null
)

class ChatViewModel(
    private val chatId: String,
    private val navigationProvider: NavigationProvider,
    private val repository: ChatRepository,
) : BaseViewModel<ChatState>(ChatState()) {

    private var loadJob: Job? = null

    // Демонстрация комбинирования потоков (combine) для сборки состояния
    private val _refreshTrigger = MutableStateFlow(0)
    
    /**
     * StateFlow и сборка состояния.
     * Используем stateIn для превращения холодного Flow в горячий StateFlow, 
     * привязанный к жизненному циклу ViewModel.
     * SharingStarted.WhileSubscribed(5000) корректно обрабатывает повороты экрана 
     * и уход приложения в фон, предотвращая лишние запросы.
     */
    val combinedState: StateFlow<ChatState> = combine(
        _state,
        _refreshTrigger
    ) { currentState, _ ->
        // Логика обогащения данных из разных источников
        currentState
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ChatState()
    )

    init {
        loadMessages(chatId)
    }

    /**
     * Coroutines: параллельное выполнение и Structured Concurrency.
     * 
     * 1. async/await: выполняем запросы к БД/Сети параллельно, не блокируя UI.
     * 2. Dispatchers.IO: явно указываем контекст для IO-операций.
     * 3. coroutineScope: гарантирует структурированность — если один запрос упадет, 
     *    второй будет отменен автоматически (Structured Concurrency).
     * 4. Job cancellation: при повторном вызове старая задача отменяется, предотвращая Race Conditions.
     */
    fun loadMessages(chatId: String, isRetry: Boolean = false) {
        loadJob?.cancel() 
        loadJob = launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                // Симуляция задержки сети для демонстрации отмены
                if (isRetry) delay(500) 
                
                coroutineScope {
                    val messagesDeferred = async(Dispatchers.IO) { repository.getMessages(chatId) }
                    val chatDeferred = async(Dispatchers.IO) { repository.getChatById(chatId) }

                    val messages = messagesDeferred.await()
                    val chat = chatDeferred.await()

                    _state.update { 
                        it.copy(
                            messages = messages, 
                            chatName = chat.title,
                            isLoading = false 
                        ) 
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    /**
     * Буферизации и Backpressure.
     * StateFlow по своей природе имеет встроенный буфер размера 1 с политикой 
     * DROP_OLDEST (conflation), что идеально подходит для UI состояний:
     * подписчик всегда получает только самое актуальное состояние, пропуская промежуточные.
     */
    fun retryLoading() {
        _refreshTrigger.value++
        loadMessages(chatId, isRetry = true)
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
                _state.update { it.copy(error = e.message) }
            } finally {
                _state.update { it.copy(sendingMessage = false) }
            }
        }
    }

    fun onBackPressed() {
        navigationProvider.navController.popBackStack()
    }
}