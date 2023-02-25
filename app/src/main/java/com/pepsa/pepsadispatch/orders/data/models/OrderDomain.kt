package com.pepsa.pepsadispatch.orders.data.models

import com.google.android.gms.maps.model.LatLng

data class OrderEntity(
    val orderId: Long,
    val dateTime: String,
    val pickupStore: String,
    val receiverName: String,
    val address_1: String,
    val address_2: String,
    val landMark1: String,
    val landMark2: String,
    val taskPrice: String,
    val pickUpLongitude: Long,
    val pickUpLatitude: Long,
    val destinationCoordinate: LatLng,
    val pickupStorePhoneContact: String,
    val receiverPhoneNumber: String
)
