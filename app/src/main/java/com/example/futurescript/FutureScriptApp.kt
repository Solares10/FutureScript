package com.example.futurescript

import android.app.Application
import com.example.futurescript.util.createNotificationChannelIfNeeded
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FutureScriptApp : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannelIfNeeded(this)
    }
}
