package app.test.demochat.presentation.screens.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AutoReplyViewModel : ViewModel() {

    private val _seconds = MutableStateFlow(5)
    val seconds: StateFlow<Int> = _seconds.asStateFlow()

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    private var job: Job? = null

    fun startTimer() {
        job?.cancel()

        job = viewModelScope.launch {
            for (i in 5 downTo 0) {
                _seconds.value = i
                if (i == 2) { // Намеренная ошибка для демонстрации отладки
                    throw IllegalStateException("Something went wrong")
                }
                delay(1000)

                if (i == 0) {
                    _event.emit(UiEvent.TimerFinished)
                }
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