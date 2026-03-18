package app.test.demochat.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface ChatDao {
    @Query("SELECT * FROM chats ORDER BY timestamp DESC")
    suspend fun getAllChats(): List<ChatEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChats(chats: List<ChatEntity>)

    /**
     * Транзакции и батч-обработки.
     * @Transaction гарантирует, что либо все чаты удалятся и вставятся новые,
     * либо состояние базы не изменится (атомарность).
     */
    @Transaction
    suspend fun updateAllChats(newChats: List<ChatEntity>) {
        deleteAll()
        insertChats(newChats)
    }

    @Query("DELETE FROM chats")
    suspend fun deleteAll()
}