package com.example.futurescript.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.futurescript.R

const val CHANNEL_ID = "future_script_letters"

fun createNotificationChannelIfNeeded(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            CHANNEL_ID, "Future Letters", NotificationManager.IMPORTANCE_DEFAULT
        )
        val mgr = context.getSystemService(NotificationManager::class.java)
        mgr.createNotificationChannel(channel)
    }
}

fun notifyLetter(context: Context, id: Int, message: String) {
    val notif = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("A letter from your past self 💌")
        .setContentText(message.take(60))
        .setStyle(NotificationCompat.BigTextStyle().bigText(message))
        .setAutoCancel(true)
        .build()
    NotificationManagerCompat.from(context).notify(id, notif)
}
