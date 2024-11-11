package app.test.demochat.presentation.screens.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.test.demochat.data.model.Message
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatId: String,
) {

    val viewModel = koinViewModel<ChatViewModel> { parametersOf(chatId) }

    val state by viewModel.state.collectAsState()

    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.scrollToItem(state.messages.size - 1)
        }
    }

    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(state.chatName) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onBackPressed() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Column {
                if (state.sendingMessage) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        placeholder = { Text("Type a message") },
                        enabled = !state.sendingMessage
                    )
                    IconButton(
                        onClick = {
                            viewModel.sendMessage(messageText)
                            messageText = ""
                        },
                        enabled = messageText.isNotBlank() && !state.sendingMessage
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send"
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = listState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.messages) { message ->
                MessageBubble(message, state)
            }
        }
    }
}

@Composable
private fun MessageBubble(message: Message, state: ChatState) {
    val isCurrentUser = remember(message) {
        message.senderId == state.currentUser
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            color = if (isCurrentUser)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(12.dp),
            tonalElevation = 1.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .widthIn(max = 280.dp)
            ) {
                Text(
                    text = message.text,
                    color = if (isCurrentUser)
                        Color.White
                    else
                        MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = SimpleDateFormat("HH:mm", Locale.getDefault())
                        .format(Date(message.timestamp)),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isCurrentUser)
                        Color.White.copy(alpha = 0.7f)
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}