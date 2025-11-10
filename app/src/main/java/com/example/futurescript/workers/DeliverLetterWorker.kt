package com.example.futurescript.workers

import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.futurescript.data.database.AppDatabase
import com.example.futurescript.util.notifyLetter
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.Instant

class DeliverLetterWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        val id = inputData.getLong("letter_id", -1L)
        val message = inputData.getString("message") ?: return Result.failure()
        if (id == -1L) return Result.failure()

        notifyLetter(applicationContext, id.toInt(), message)
        AppDatabase.get(applicationContext).letterDao().markDelivered(id)
        return Result.success()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun scheduleDelivery(context: Context, letterId: Long, deliverAtEpochSec: Long, message: String) {
    val delay = Duration.between(Instant.now(), Instant.ofEpochSecond(deliverAtEpochSec))
        .coerceAtLeast(Duration.ofSeconds(1))
    val data = Data.Builder()
        .putLong("letter_id", letterId)
        .putString("message", message)
        .build()
    val req = OneTimeWorkRequestBuilder<DeliverLetterWorker>()
        .setInputData(data)
        .setInitialDelay(delay)
        .build()
    WorkManager.getInstance(context).enqueue(req)
}
