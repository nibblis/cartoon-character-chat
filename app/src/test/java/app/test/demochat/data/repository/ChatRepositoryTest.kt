package app.test.demochat.data.repository

import app.test.demochat.data.exceptions.ChatNotFoundException
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class ChatRepositoryTest {

    private lateinit var repository: ChatRepository

    @Before
    fun setUp() {
        repository = ChatRepository()
    }

    @Test
    fun `getChatById returns chat when it exists`() {
        // Act
        val chat = repository.getChatById("cartman_1")
        
        // Assert
        assertThat(chat.title).isEqualTo("Eric Cartman")
    }

    @Test
    fun `getChatById throws ChatNotFoundException for non-existent id`() {
        // Assert
        assertThrows(ChatNotFoundException::class.java) {
            // Act
            repository.getChatById("unknown_id")
        }
    }

    @Test
    fun `getTotalUnreadCount calculates sum correctly`() {
        // Act
        val total = repository.getTotalUnreadCount()
        
        // Assert
        // Основываясь на моковых данных (3+1+5+0+2+1+4+0+2+1 = 19)
        assertThat(total).isEqualTo(19)
    }
}