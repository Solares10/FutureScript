package com.example.futurescript.workers

import android.app.Application
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import java.util.concurrent.TimeUnit

fun scheduleDelivery(app: Application, letterId: Long, deliverAtMillis: Long) {
    val delay = deliverAtMillis - System.currentTimeMillis()
    if (delay <= 0) return

    val data = Data.Builder()
        .putLong("letterId", letterId)
        .build()

    val workRequest: WorkRequest =
        OneTimeWorkRequestBuilder<DeliverLetterWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

    WorkManager.getInstance(app).enqueue(workRequest)
}
