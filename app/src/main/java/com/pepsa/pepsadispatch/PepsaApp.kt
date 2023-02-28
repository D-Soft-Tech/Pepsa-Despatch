package com.pepsa.pepsadispatch

import android.app.Application
import android.content.ContextWrapper
import android.util.Log
import androidx.work.Configuration
import com.google.firebase.FirebaseApp
import com.pixplicity.easyprefs.library.Prefs
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.util.concurrent.Executors

@HiltAndroidApp
class PepsaApp : Application(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setUseDefaultSharedPreference(true)
            .build()
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setExecutor(Executors.newSingleThreadExecutor())
            .setMinimumLoggingLevel(Log.VERBOSE).build()
}
