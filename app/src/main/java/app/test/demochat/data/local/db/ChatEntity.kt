package app.test.demochat.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey val id: String,
    val title: String,
    val lastMessage: String?,
    val timestamp: Long,
    val unreadCount: Int,
    // Новое поле, добавленное в версии 2
    val isPinned: Int = 0
)