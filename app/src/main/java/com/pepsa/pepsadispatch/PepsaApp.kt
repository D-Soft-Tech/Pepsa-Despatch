package com.pepsa.pepsadispatch

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import android.widget.Toast
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
        FirebaseApp.initializeApp(this)
        Timber.plant(Timber.DebugTree())
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setUseDefaultSharedPreference(true)
            .build()
        checkConnectivityChange(applicationContext)
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setExecutor(Executors.newSingleThreadExecutor())
            .setMinimumLoggingLevel(Log.VERBOSE).build()

    private fun checkConnectivityChange(context: Context) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest: NetworkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .build()
        connectivityManager.registerNetworkCallback(
            networkRequest,
            object :
                ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    Toast.makeText(
                        context,
                        getString(R.string.connection_available),
                        Toast.LENGTH_SHORT,
                    ).show()
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    Toast.makeText(
                        context,
                        getString(R.string.connection_unavailable),
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            },
        )
    }
}
