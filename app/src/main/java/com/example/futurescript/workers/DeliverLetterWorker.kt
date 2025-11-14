package com.example.futurescript.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.futurescript.R
import com.example.futurescript.data.repository.LetterRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class DeliverLetterWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repo: LetterRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            // ✅ Correct way to retrieve input data from WorkManager
            val letterId = inputData.getLong("letterId", -1L)
            if (letterId == -1L) {
                return@withContext Result.failure()
            }

            // ✅ Mark the letter as delivered in the database
            repo.markDelivered(letterId)

            // ✅ Optional: show user notification when letter is delivered
            showDeliveryNotification(letterId)

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    private fun showDeliveryNotification(letterId: Long) {
        val notificationManager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "letter_delivery_channel"

        // Create channel if needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Letter Delivery",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val notification = NotificationCompat.Builder(appContext, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Letter Delivered 🎉")
            .setContentText("Your scheduled letter (ID: $letterId) has been delivered.")
            .setAutoCancel(true)
            .build()

        // Show it
        notificationManager.notify(letterId.toInt(), notification)
    }
}
