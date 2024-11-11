package app.test.demochat.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatItem(
    val id: String,
    val title: String,
    val lastMessage: String?,
    val timestamp: Long,
    val unreadCount: Int = 0,
    val avatarUrl: String? = null
)