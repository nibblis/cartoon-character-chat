package app.test.demochat.presentation.screens.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import app.test.demochat.utils.NotificationHelper

@Composable
fun AutoReplyScreen(
    viewModel: AutoReplyViewModel = viewModel()
) {
    val context = LocalContext.current
    val seconds by viewModel.seconds.collectAsState()

    // Запуск таймера один раз при входе на экран
    LaunchedEffect(Unit) {
        viewModel.startTimer()
    }

    // Подписка на события
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            if (event is AutoReplyViewModel.UiEvent.TimerFinished) {
                NotificationHelper.showNotification(
                    context.applicationContext
                )
            }
        }
    }

    // Остановка таймера при уходе с экрана
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopTimer()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Auto reply in $seconds seconds")
    }
}