package app.test.demochat.presentation.screens.test

import app.test.demochat.presentation.screens.test.rules.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AutoReplyViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: AutoReplyViewModel

    @Before
    fun setUp() {
        viewModel = AutoReplyViewModel()
    }

    // --- Happy Path --- 
    @Test
    fun `timer ticks down correctly`() = runTest {
        // Arrange
        val expectedSeconds = listOf(5, 4, 3, 2, 1, 0)
        val actualSeconds = mutableListOf<Int>()
        val job = launch { viewModel.seconds.collect { actualSeconds.add(it) } }

        // Act
        viewModel.startTimer()
        advanceUntilIdle()

        // Assert
        assertThat(actualSeconds).isEqualTo(expectedSeconds)
        job.cancel()
    }

    @Test
    fun `timer emits finished event when done`() = runTest {
        // Arrange
        var event: AutoReplyViewModel.UiEvent? = null
        val job = launch { event = viewModel.event.first() }

        // Act
        viewModel.startTimer()
        advanceUntilIdle()

        // Assert
        assertThat(event).isInstanceOf(AutoReplyViewModel.UiEvent.TimerFinished::class.java)
        job.cancel()
    }

    // --- Negative Case ---
    @Test
    fun `starting timer twice restarts it`() = runTest {
        val secondsList = mutableListOf<Int>()
        val job = launch { viewModel.seconds.collect { secondsList.add(it) } }

        // Act
        viewModel.startTimer() // Запускаем первый раз
        advanceTimeBy(2000)
        runCurrent()

        viewModel.startTimer() // Запускаем второй раз
        advanceTimeBy(2000)
        runCurrent()

        // Assert
        // Ожидаем: 5 (старт), 4, 3 (первый таймер), 5 (рестарт), 4, 3 (второй таймер)
        assertThat(secondsList).isEqualTo(listOf(5, 4, 3, 5, 4, 3))
        job.cancel()
    }

    // --- Edge Case ---
    @Test
    fun `stopping timer that was not started does nothing`() = runTest {
        val secondsList = mutableListOf<Int>()
        val job = launch { viewModel.seconds.collect { secondsList.add(it) } }

        // Act
        viewModel.stopTimer()
        advanceUntilIdle()

        // Assert
        // Ожидаем, что значение не изменилось с дефолтного
        assertThat(secondsList).containsExactly(5)
        job.cancel()
    }
}