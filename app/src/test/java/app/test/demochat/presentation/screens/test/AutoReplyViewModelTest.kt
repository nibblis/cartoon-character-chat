package app.test.demochat.presentation.screens.test

import app.test.demochat.presentation.screens.test.rules.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
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

    @Test
    fun `test timer ticks down correctly`() = runTest {
        // Arrange
        val expectedSeconds = listOf(5, 4, 3, 2, 1, 0)
        val actualSeconds = mutableListOf<Int>()

        val job = launch {
            viewModel.seconds.collect { actualSeconds.add(it) }
        }

        // Act
        viewModel.startTimer()
        advanceTimeBy(6000)

        // Assert
        assertThat(actualSeconds).isEqualTo(expectedSeconds)
        job.cancel()
    }

    @Test
    fun `test timer emits finished event`() = runTest {
        // Arrange
        var event: AutoReplyViewModel.UiEvent? = null
        val job = launch {
            event = viewModel.event.first()
        }

        // Act
        viewModel.startTimer()
        advanceTimeBy(6000)

        // Assert
        assertThat(event).isInstanceOf(AutoReplyViewModel.UiEvent.TimerFinished::class.java)
        job.cancel()
    }

    @Test
    fun `test stop timer`() = runTest {
        // Arrange
        val secondsList = mutableListOf<Int>()
        val job = launch {
            viewModel.seconds.collect{
                secondsList.add(it)
            }
        }
        viewModel.startTimer()
        advanceTimeBy(2000)

        // Act
        viewModel.stopTimer()
        advanceTimeBy(3000)

        // Assert
        assertThat(secondsList).isEqualTo(listOf(5,4,3))
        job.cancel()
    }
}
