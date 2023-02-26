package com.pepsa.pepsadispatch.orders.data.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.pepsa.pepsadispatch.R
import com.pepsa.pepsadispatch.mian.presentation.ui.activities.MainAppActivity
import com.pepsa.pepsadispatch.orders.data.models.OrderEntity
import com.pepsa.pepsadispatch.orders.utils.DeliveryOrdersConstants.INCOMING_ORDER_NOTIFICATION_CHANNEL_ID
import com.pepsa.pepsadispatch.orders.utils.DeliveryOrdersConstants.INT_INCOMING_NOTIFICATION_ID
import com.pepsa.pepsadispatch.orders.utils.DeliveryOrdersConstants.INT_INCOMING_ORDER_PENDING_INTENT_REQUEST_CODE
import com.pepsa.pepsadispatch.orders.utils.DeliveryOrdersConstants.STRING_INCOMING_ORDER_INTENT_ACTION
import com.pepsa.pepsadispatch.orders.utils.DeliveryOrdersConstants.TAG_INCOMING_ORDER_RECEIVED
import timber.log.Timber

class GetOrderFirebaseMessagingService : FirebaseMessagingService() {
    private val gson: Gson = Gson()
    override fun onNewToken(token: String) {
        scheduleJobToUpdateDeviceTokenToServer()
        super.onNewToken(token)
    }

    override fun onMessageReceived(deliveryOrder: RemoteMessage) {
        if (deliveryOrder.data.isNotEmpty()) {
            val order = deliveryOrder.data["DeliveryOrderRequest"]
            val taskOrder = order?.let {
                gson.fromJson(it, OrderEntity::class.java)
            }
            taskOrder?.let {
                createNotification(it)
                startRinging()
            }
        }
    }

    private fun createNotification(incomingOrder: OrderEntity) {
        val incomingOrderIntent = Intent(this, MainAppActivity::class.java)
        incomingOrderIntent.apply {
            action = STRING_INCOMING_ORDER_INTENT_ACTION
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra(TAG_INCOMING_ORDER_RECEIVED, true)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            INT_INCOMING_ORDER_PENDING_INTENT_REQUEST_CODE,
            incomingOrderIntent,
            PendingIntent.FLAG_IMMUTABLE,
        )
        val defaultRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder =
            NotificationCompat.Builder(this, INCOMING_ORDER_NOTIFICATION_CHANNEL_ID)
                .setContentTitle(getString(R.string.incoming_order_from_pd))
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(getString(R.string.order_from) + incomingOrder.pickupStore + "\n" + incomingOrder.address_1),
                )
                .setContentText(getString(R.string.order_from) + incomingOrder.pickupStore + "\n" + incomingOrder.address_1)
                .setSmallIcon(R.drawable.png_pepsa_dispatch_logo)
                .setAutoCancel(true)
                .setSound(defaultRingtone)
                .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                INCOMING_ORDER_NOTIFICATION_CHANNEL_ID,
                getString(R.string.incoming_order_from_pd),
                NotificationManager.IMPORTANCE_HIGH,
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(INT_INCOMING_NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun startRinging() {
        try {
            val ringTone = MediaPlayer.create(applicationContext, R.raw.ring_tone_one)
            ringTone?.apply {
                isLooping = true
                startRinging()
            }
        } catch (e: Exception) {
            Timber.d("RING_TONE_ERROR===>%s", e.localizedMessage)
        }
    }

    private fun scheduleJobToUpdateDeviceTokenToServer() {
    }
}
