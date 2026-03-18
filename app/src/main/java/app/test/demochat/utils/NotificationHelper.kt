package app.test.demochat.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * Помощник для работы с уведомлениями.
 * Демонстрирует создание каналов (Notification Channels) и категорий.
 */
class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_MESSAGES_ID = "messages_channel"
        const val CHANNEL_SYSTEM_ID = "system_channel"
    }

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Канал для сообщений (Высокий приоритет)
            val messagesChannel = NotificationChannel(
                CHANNEL_MESSAGES_ID,
                "Новые сообщения",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Уведомления о новых сообщениях от персонажей"
            }

            // Канал для системных событий (Низкий приоритет)
            val systemChannel = NotificationChannel(
                CHANNEL_SYSTEM_ID,
                "Системные уведомления",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Информация о работе приложения и синхронизации"
            }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(messagesChannel)
            manager.createNotificationChannel(systemChannel)
        }
    }

    fun showMessageNotification(title: String, message: String) {
        val builder = NotificationCompat.Builder(context, CHANNEL_MESSAGES_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_email)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            // В реальном приложении здесь должна быть проверка разрешения POST_NOTIFICATIONS
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }
}