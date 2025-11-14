package com.example.futurescript.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.futurescript.R
import com.example.futurescript.ui.openletter.OpenLetterActivity

class LetterNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        // 🔍 Debug log to confirm broadcast was received
        Log.d("FutureScript", "📬 LetterNotificationReceiver fired! Message=" + intent?.getStringExtra("message"))

        Log.d("FutureScript", "LetterNotificationReceiver triggered")

        // Ensure this is the right broadcast action
        if (intent?.action != "com.example.futurescript.ACTION_SEND_LETTER") {
            Log.w("FutureScript", "Received unrelated intent action: ${intent?.action}")
            return
        }

        val message = intent.getStringExtra("message")
            ?: "💌 You have a message from your past self!"

        // 🎯 Intent to open your envelope animation screen
        val openIntent = Intent(context, OpenLetterActivity::class.java).apply {
            putExtra("message", message)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 🔔 Build the notification
        val channelId = "future_script_channel_v3" // new ID to refresh blocked channels
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_email)   // guaranteed visible icon
            .setContentTitle("Future Script")
            .setContentText("💌 " + message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(0, 500, 250, 500))          // vibrate pattern for attention
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)


        // 🔧 Create notification channel (Android O+)
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Future Script Letters",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for letters from your past self 💌"
            }
            manager.createNotificationChannel(channel)
        }

        // 🚀 Post the notification
        manager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
        Log.d("FutureScript", "Notification created: $message")
    }
}
