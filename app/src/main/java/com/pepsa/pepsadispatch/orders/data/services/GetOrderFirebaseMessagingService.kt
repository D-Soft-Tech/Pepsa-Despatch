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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.pepsa.pepsadispatch.R
import com.pepsa.pepsadispatch.maps.domain.models.MapData
import com.pepsa.pepsadispatch.maps.utils.MapUtils
import com.pepsa.pepsadispatch.mian.presentation.ui.activities.MainAppActivity
import com.pepsa.pepsadispatch.orders.data.models.OrderEntity
import com.pepsa.pepsadispatch.orders.data.workers.GetOrderRouteWorker
import com.pepsa.pepsadispatch.orders.domain.models.OrderDomain
import com.pepsa.pepsadispatch.orders.utils.DeliveryOrdersConstants.INCOMING_ORDER_NOTIFICATION_CHANNEL_ID
import com.pepsa.pepsadispatch.orders.utils.DeliveryOrdersConstants.INT_INCOMING_NOTIFICATION_ID
import com.pepsa.pepsadispatch.orders.utils.DeliveryOrdersConstants.INT_INCOMING_ORDER_PENDING_INTENT_REQUEST_CODE
import com.pepsa.pepsadispatch.orders.utils.DeliveryOrdersConstants.STRING_GET_ROUTE_ORDER_WORKER_INPUT_DATA_TAG
import com.pepsa.pepsadispatch.orders.utils.DeliveryOrdersConstants.STRING_GET_ROUTE_ORDER_WORKER_OUTPUT_DATA_TAG
import com.pepsa.pepsadispatch.orders.utils.DeliveryOrdersConstants.STRING_INCOMING_ORDER_INTENT_ACTION
import com.pepsa.pepsadispatch.orders.utils.DeliveryOrdersConstants.TAG_INCOMING_ORDER_RECEIVED
import com.pepsa.pepsadispatch.orders.utils.mappers.EntityMappers.toDomain
import timber.log.Timber

class GetOrderFirebaseMessagingService : FirebaseMessagingService(), LifecycleOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)
    private val gson: Gson = Gson()
    private val workManagerInstance: WorkManager = WorkManager.getInstance(this)
    private val mapsUtils: MapUtils = MapUtils(gson)

    override fun onCreate() {
        super.onCreate()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    override fun onNewToken(token: String) {
        Timber.d("NEW_TOKEN_TOKEN=====>%s", token)
    }

    override fun onMessageReceived(deliveryOrder: RemoteMessage) {
        if (deliveryOrder.data.isNotEmpty()) {
            val order = deliveryOrder.data["DeliveryOrderRequest"]
            val taskOrder = order?.let {
                gson.fromJson(it, OrderEntity::class.java)
            }
            taskOrder?.let {
                // Get the route first
                scheduleWorkToGetOrderRoute(it)
            }
        }
    }

    private fun createNotification(incomingOrder: OrderDomain) {
        val incomingOrderIntent = Intent(this, MainAppActivity::class.java)
        incomingOrderIntent.apply {
            action = STRING_INCOMING_ORDER_INTENT_ACTION
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra(TAG_INCOMING_ORDER_RECEIVED, gson.toJson(incomingOrder))
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
        // https://www.youtube.com/watch?v=5Bo1rOgWS-Y&ab_channel=LevitesWATCHTV
    }

    private fun scheduleWorkToGetOrderRoute(incomingOrder: OrderEntity) {
        val order = gson.toJson(incomingOrder)
        val inputData: Data = Data.Builder()
            .putString(STRING_GET_ROUTE_ORDER_WORKER_INPUT_DATA_TAG, order)
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest = OneTimeWorkRequest.Builder(GetOrderRouteWorker::class.java)
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()
        workManagerInstance.beginWith(workRequest).enqueue()
        listenForWorkManagerOutputData(workRequest, incomingOrder)
    }

    private fun listenForWorkManagerOutputData(
        workRequest: WorkRequest,
        inComingOrderEntity: OrderEntity,
    ) {
        workManagerInstance.getWorkInfoByIdLiveData(workRequest.id)
            .observe(this) { workInfo ->
                if (workInfo != null && workInfo.state === WorkInfo.State.SUCCEEDED) {
                    val outputData = workInfo.outputData
                    val response =
                        outputData.getString(STRING_GET_ROUTE_ORDER_WORKER_OUTPUT_DATA_TAG)
                    response?.let {
                        if (it.isNotEmpty()) {
                            val mapData = gson.fromJson(it, MapData::class.java)
                            val distance =
                                mapsUtils.mapMapDataToArrayListOfLatLng(mapData).second.first
                            val duration =
                                mapsUtils.mapMapDataToArrayListOfLatLng(mapData).second.second
                            val orderEntity = inComingOrderEntity.toDomain(distance, duration)
                            createNotification(orderEntity)
                            startRinging()
                        }
                    }
                }
            }
    }
}
