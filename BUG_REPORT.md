### Баг-репорт: Сбой таймера в AutoReplyViewModel

**Заголовок:** Сбой в `AutoReplyViewModel`, когда таймер достигает 2 секунд.

**Описание:**
Приложение падает с ошибкой `IllegalStateException`, когда таймер обратного отсчета достигает 2 секунд. Это мешает таймеру завершить обратный отсчет и отправить событие `TimerFinished`.

**Шаги для воспроизведения:**
1. Создать экземпляр `AutoReplyViewModel`.
2. Вызвать метод `startTimer()`.
3. Наблюдать за сбоем приложения или падением теста, когда значение таймера становится равным 2.

**Стек-трейс / Лог сбоя:**
Падающий юнит-тест `test timer ticks down correctly` предоставляет следующий подавленный стек-трейс, который четко указывает на источник исключения:

```
java.lang.IllegalStateException: Something went wrong
    at app.test.demochat.presentation.screens.test.AutoReplyViewModel$startTimer$1.invokeSuspend(AutoReplyViewModel.kt:31)
    at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33)
    at kotlinx.coroutines.DispatchedTaskKt.resume(DispatchedTask.kt:231)
    ...
```

**Анализ основной причины:**
В методе `startTimer` в файле `AutoReplyViewModel.kt` на строке 31 было обнаружено условное выражение. Это условие намеренно выбрасывает `IllegalStateException`, когда переменная таймера `i` равна точно 2, что и вызывает сбой.

```kotlin
// AutoReplyViewModel.kt
job = viewModelScope.launch {
    for (i in 5 downTo 0) {
        _seconds.value = i
        if (i == 2) { // Это причина сбоя
            throw IllegalStateException("Something went wrong")
        }
        delay(1000)

        if (i == 0) {
            _event.emit(UiEvent.TimerFinished)
        }
    }
}
```

**Окружение:**
- **ОС:** macOS (видно из путей к файлам)
- **Устройство:** Локальная JVM (воспроизведено через юнит-тест)
- **Версия приложения:** 1.0
