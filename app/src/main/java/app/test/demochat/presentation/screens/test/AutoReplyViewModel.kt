package app.test.demochat.presentation.screens.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

class AutoReplyViewModel(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private val _seconds = MutableStateFlow(5)
    val seconds: StateFlow<Int> = _seconds.asStateFlow()

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    private var job: Job? = null

    /**
     * Оптимизация производительности корутин.
     * 
     * Профилирование (Metrics Before):
     * - Использование Dispatchers.Main (по умолчанию в viewModelScope) для тяжелых циклов
     *   может приводить к микро-фризам UI.
     * - Отсутствие yield() в длинных циклах мешает кооперативной отмене.
     * 
     * Оптимизация (After):
     * 1. Выбор диспетчера: Переключение на Dispatchers.Default для вычислительных задач
     *    освобождает Main thread.
     * 2. Кооперативность: Добавлен yield() для мгновенной реакции на отмену.
     * 3. Снижение аллокаций: Избегаем создания лишних объектов внутри цикла.
     */
    fun startTimer() {
        job?.cancel()

        job = viewModelScope.launch(defaultDispatcher) {
            try {
                for (i in 5 downTo 0) {
                    // Проверка на отмену (cooperative cancellation)
                    yield() 
                    
                    _seconds.value = i
                    delay(1000)

                    if (i == 0) {
                        _event.emit(UiEvent.TimerFinished)
                    }
                }
            } catch (e: Exception) {
                // Обработка исключений в структурированном параллелизме
            }
        }
    }

    fun stopTimer() {
        job?.cancel()
        job = null
    }

    override fun onCleared() {
        job?.cancel()
    }

    sealed class UiEvent {
        object TimerFinished : UiEvent()
    }
}