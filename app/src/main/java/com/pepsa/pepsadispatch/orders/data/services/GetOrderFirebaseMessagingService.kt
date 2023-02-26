package com.pepsa.pepsadispatch.orders.data.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.pepsa.pepsadispatch.orders.data.models.OrderEntity

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
        }
    }

    private fun scheduleJobToUpdateDeviceTokenToServer() {
    }
}
