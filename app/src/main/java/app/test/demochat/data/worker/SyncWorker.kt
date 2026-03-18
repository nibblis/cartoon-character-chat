package app.test.demochat.data.worker

import android.content.Context
import android.util.Log
import androidx.work.*
import androidx.work.CoroutineWorker
import java.util.concurrent.TimeUnit

/**
 * Устойчивая фоновая задача для синхронизации данных.
 * Демонстрирует использование WorkManager с ретраями и ограничениями.
 */
class SyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val attempt = runAttemptCount
        Log.i("SyncWorker", "Запуск синхронизации. Попытка №$attempt")

        return try {
            // Имитация сетевой работы
            performSync()
            
            Log.i("SyncWorker", "Синхронизация успешно завершена")
            Result.success()
        } catch (e: Exception) {
            if (attempt < 3) {
                Log.w("SyncWorker", "Ошибка синхронизации, планируем ретрай через экспоненциальную задержку")
                // Ретрай (Retry) — встроенный механизм WorkManager
                Result.retry()
            } else {
                Log.e("SyncWorker", "Превышено количество попыток, фатальная ошибка")
                Result.failure()
            }
        }
    }

    private suspend fun performSync() {
        // Логика отправки сообщений из локальной БД на сервер
    }

    companion object {
        private const val SYNC_TAG = "periodic_sync"

        /**
         * Настройка периодической фоновой задачи с ограничениями.
         */
        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED) // Только при наличии сети
                .setRequiresBatteryNotLow(true) // Только если заряд не критический
                .build()

            val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(1, TimeUnit.HOURS)
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL, // Экспоненциальный ретрай
                    WorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .addTag(SYNC_TAG)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                SYNC_TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                syncRequest
            )
        }
    }
}