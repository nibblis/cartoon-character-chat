package app.test.demochat.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [ChatEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao

    companion object {
        /**
         * Миграция с версии 1 на 2.
         * Добавляем колонку 'isPinned' в таблицу 'chats'.
         * 
         * Room выполняет миграции внутри транзакции.
         * Если SQL-команда упадет, база откатится к состоянию версии 1.
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE chats ADD COLUMN isPinned INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}